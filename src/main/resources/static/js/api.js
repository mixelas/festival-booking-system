// HTTP helper module with Bearer token support from localStorage
const CFG = window.APP_CONFIG || {};
const BASE = (CFG.API_BASE || "/api").replace(/\/+$/,"");

function getToken() {
  return localStorage.getItem("access_token") || null;
}

export function setToken(token) {
  if (token) localStorage.setItem("access_token", token);
  else localStorage.removeItem("access_token");
}

/**
 * Core HTTP request function
 * @param {string} path - Request path (relative or absolute)
 * @param {Object} options - Request options
 * @param {string} options.method - HTTP method (default: GET)
 * @param {Object} options.data - Request body data
 * @param {Object} options.headers - Additional headers
 * @param {Object} options.params - Query parameters
 * @returns {Promise<Object|string|null>} Response data
 */
async function httpRequest(path, { method="GET", data, headers={}, params } = {}) {
  // Safely construct URL
  let url;
  if (path.startsWith("http://") || path.startsWith("https://")) {
    url = new URL(path);
  } else if (path.startsWith(BASE + "/")) {
    url = new URL(path, window.location.origin);
  } else if (path.startsWith("/")) {
    url = new URL(path, window.location.origin);
  } else {
    url = new URL(`${BASE}/${path}`, window.location.origin);
  }

  // Add query parameters
  if (params && typeof params === "object") {
    for (const [k, v] of Object.entries(params)) {
      if (v == null || v === "") continue;
      if (Array.isArray(v)) v.forEach(x => url.searchParams.append(k, x));
      else url.searchParams.set(k, v);
    }
  }

  // Build request options
  const opts = {
    method,
    headers: { Accept: "application/json", ...headers },
    credentials: "include"
  };

  // Add JWT token if available
  const token = getToken();
  if (token) opts.headers.Authorization = `Bearer ${token}`;

  // Handle request body
  if (data !== undefined) {
    if (data instanceof FormData) {
      delete opts.headers["Content-Type"];
      opts.body = data;
    } else {
      opts.headers["Content-Type"] = "application/json";
      opts.body = JSON.stringify(data);
    }
  }

  // Execute request
  const res = await fetch(url.toString(), opts);

  // Handle empty responses
  if (res.status === 204 || res.status === 205) return null;

  // Determine response type
  const ct = res.headers.get("content-type") || "";
  const isJson = ct.includes("application/json");

  // Handle errors
  if (!res.ok) {
    const payload = isJson ? await res.json().catch(()=>null) : await res.text().catch(()=>null);
    const err = new Error(`HTTP ${res.status}`);
    err.status = res.status;
    err.payload = payload;
    throw err;
  }

  // Return parsed response
  return isJson ? (await res.json().catch(()=>null)) : (await res.text().catch(()=>null));
}

// Convenience method objects
export const http = {
  async get(path, options) {
    return httpRequest(path, { ...options, method: "GET" });
  },
  async post(path, data, options) {
    return httpRequest(path, { ...options, data, method: "POST" });
  },
  async put(path, data, options) {
    return httpRequest(path, { ...options, data, method: "PUT" });
  },
  async patch(path, data, options) {
    return httpRequest(path, { ...options, data, method: "PATCH" });
  },
  async delete(path, options) {
    return httpRequest(path, { ...options, method: "DELETE" });
  }
};

// Also export httpRequest for advanced usage
export { httpRequest };
