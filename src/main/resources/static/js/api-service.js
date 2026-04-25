

export const getCsrf = () => ({
  token: document.querySelector('meta[name="_csrf"]')?.getAttribute("content"),
  header: document.querySelector('meta[name="_csrf_header"]')?.getAttribute("content")
});

async function apiRequest(url, method, body = null) {
  const { token, header } = getCsrf();
  const options = {
    method,
    headers: { "Content-Type": "application/json" }
  };
  
  if (header && token) options.headers[header] = token;
  if (body) options.body = JSON.stringify(body);

  const response = await fetch(url, options);
  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || "Server error occurred");
  }
  return response;
}

export const EmployeeAPI = {
  checkUsername: (username) => fetch(`/api/employee/check-username?username=${username}`).then(res => res.json()),
  create: (payload) => apiRequest("/api/employee", "POST", payload),
  delete: (id) => apiRequest(`/api/employee/${id}`, "DELETE")
};

export const ItemAPI = {
  save: (type, payload, isCreate) => apiRequest(`/api/${type}`, isCreate ? "POST" : "PATCH", payload),
  delete: (type, id) => apiRequest(`/api/${type}/${id}`, "DELETE")
};

export const BossAPI = {
  getAll: () => fetch("/api/boss").then(res => res.json()),
  getMail: () => apiRequest("/api/getmail", "GET")
};