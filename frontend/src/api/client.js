const base = import.meta.env.VITE_API_BASE || 'http://localhost:8080/api/v1'

async function request(path, options = {}) {
    const token = localStorage.getItem('taskflow_access_token')
    const response = await fetch(`${base}${path}`, {
        headers: {'Content-Type': 'application/json', ...(token ? {Authorization: `Bearer ${token}`} : {}), ...(options.headers || {})},
        ...options
    })
    if (response.status === 401) {
        localStorage.removeItem('taskflow_access_token')
        localStorage.removeItem('taskflow_user')
        window.dispatchEvent(new Event('taskflow:logout'))
    }
    if (!response.ok) throw new Error((await response.json().catch(() => ({}))).message || '请求失败')
    return response.json()
}

export const api = {
    login: (payload) => request('/auth/login', {method: 'POST', body: JSON.stringify(payload)}),
    dashboard: () => request('/dashboard'),
    projects: () => request('/projects'),
    tasks: (params = {}) => request(`/tasks?${new URLSearchParams(Object.entries(params).filter(([, v]) => v))}`),
    createProject: (payload) => request('/projects', {method: 'POST', body: JSON.stringify(payload)}),
    createTask: (projectId, payload) => request(`/projects/${projectId}/tasks`, {method: 'POST', body: JSON.stringify(payload)}),
    updateStatus: (id, status) => request(`/tasks/${id}/status`, {method: 'PATCH', body: JSON.stringify({status})})
}
