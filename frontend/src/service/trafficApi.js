import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '/api'
})

export const submitEvent   = (data) => api.post('/events', data)
export const getViolations = ()     => api.get('/violations')