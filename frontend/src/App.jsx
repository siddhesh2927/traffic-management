import { useCallback, useEffect, useMemo, useState } from 'react'
import axios from 'axios'
import './App.css'

function App() {
  const apiBase = useMemo(
    () => (import.meta.env.VITE_API_BASE_URL || '').replace(/\/$/, ''),
    [],
  )

  const [eventForm, setEventForm] = useState({
    licensePlate: '',
    speed: '',
    speedLimit: '80',
    location: '',
  })
  const [submitState, setSubmitState] = useState({ loading: false, message: '', isViolation: false })
  const [violations, setViolations] = useState([])
  const [stats, setStats] = useState(null)
  const [loadingList, setLoadingList] = useState(false)
  const [error, setError] = useState('')

  const handleFormChange = (e) => {
    const { name, value } = e.target
    setEventForm((prev) => ({ ...prev, [name]: value }))
  }

  const loadViolations = useCallback(async () => {
    setLoadingList(true)
    setError('')
    try {
      const [violationsRes, statsRes] = await Promise.all([
        axios.get(`${apiBase}/api/violations`),
        axios.get(`${apiBase}/api/violations/stats`),
      ])
      setViolations(violationsRes.data?.data || [])
      setStats(statsRes.data?.data || null)
    } catch {
      setError('Could not connect to backend. Make sure Spring Boot is running on port 8081.')
    } finally {
      setLoadingList(false)
    }
  }, [apiBase])

  useEffect(() => {
    const timer = setTimeout(() => {
      void loadViolations()
    }, 0)
    return () => clearTimeout(timer)
  }, [loadViolations])

  const submitEvent = async (e) => {
    e.preventDefault()
    setSubmitState({ loading: true, message: '', isViolation: false })
    setError('')

    try {
      const payload = {
        licensePlate: eventForm.licensePlate.trim(),
        speed: Number(eventForm.speed),
        speedLimit: Number(eventForm.speedLimit),
        location: eventForm.location.trim(),
      }

      const res = await axios.post(`${apiBase}/api/events`, payload)
      const isViolation = Boolean(res.data?.violation)

      setSubmitState({
        loading: false,
        message: isViolation
          ? `Violation recorded for ${payload.licensePlate}.`
          : 'No violation. Speed is within limit.',
        isViolation,
      })

      if (isViolation) {
        await loadViolations()
      }
    } catch {
      setSubmitState({ loading: false, message: '', isViolation: false })
      setError('Failed to submit event. Check request data and backend logs.')
    }
  }

  return (
    <main className="min-h-screen bg-slate-100 text-slate-900">
      <section className="mx-auto max-w-6xl p-6 md:p-10">
        <header className="mb-8 rounded-2xl bg-gradient-to-r from-blue-700 to-cyan-600 p-6 text-white shadow-lg">
          <h1 className="text-2xl font-bold md:text-3xl">Traffic Violation Dashboard</h1>
          <p className="mt-2 text-sm md:text-base">
            Submit vehicle events and track fines from the Spring Boot backend.
          </p>
        </header>

        <div className="grid gap-6 md:grid-cols-2">
          <form
            onSubmit={submitEvent}
            className="rounded-2xl bg-white p-5 shadow-sm ring-1 ring-slate-200"
          >
            <h2 className="mb-4 text-lg font-semibold">Report Vehicle Event</h2>
            <div className="grid gap-3">
              <input
                name="licensePlate"
                value={eventForm.licensePlate}
                onChange={handleFormChange}
                placeholder="License Plate (e.g., MH12AB1234)"
                className="rounded-lg border border-slate-300 px-3 py-2 outline-none focus:border-blue-600"
                required
              />
              <input
                type="number"
                name="speed"
                value={eventForm.speed}
                onChange={handleFormChange}
                placeholder="Vehicle Speed (km/h)"
                className="rounded-lg border border-slate-300 px-3 py-2 outline-none focus:border-blue-600"
                min="1"
                required
              />
              <input
                type="number"
                name="speedLimit"
                value={eventForm.speedLimit}
                onChange={handleFormChange}
                placeholder="Speed Limit (km/h)"
                className="rounded-lg border border-slate-300 px-3 py-2 outline-none focus:border-blue-600"
                min="1"
                required
              />
              <input
                name="location"
                value={eventForm.location}
                onChange={handleFormChange}
                placeholder="Location (e.g., Ring Road Sector 4)"
                className="rounded-lg border border-slate-300 px-3 py-2 outline-none focus:border-blue-600"
                required
              />
            </div>

            <button
              type="submit"
              disabled={submitState.loading}
              className="mt-4 w-full rounded-lg bg-blue-700 px-4 py-2 font-medium text-white transition hover:bg-blue-800 disabled:cursor-not-allowed disabled:bg-slate-400"
            >
              {submitState.loading ? 'Submitting...' : 'Submit Event'}
            </button>

            {submitState.message && (
              <p
                className={`mt-3 rounded-md px-3 py-2 text-sm ${
                  submitState.isViolation ? 'bg-red-50 text-red-700' : 'bg-emerald-50 text-emerald-700'
                }`}
              >
                {submitState.message}
              </p>
            )}
          </form>

          <section className="rounded-2xl bg-white p-5 shadow-sm ring-1 ring-slate-200">
            <h2 className="mb-4 text-lg font-semibold">Quick Stats</h2>
            {!stats ? (
              <p className="text-sm text-slate-500">No stats yet.</p>
            ) : (
              <div className="grid grid-cols-2 gap-3 text-sm">
                <div className="rounded-lg bg-slate-50 p-3">
                  <p className="text-slate-500">Total Violations</p>
                  <p className="text-xl font-semibold">{stats.totalViolations}</p>
                </div>
                <div className="rounded-lg bg-slate-50 p-3">
                  <p className="text-slate-500">Total Fine</p>
                  <p className="text-xl font-semibold">Rs {Math.round(stats.totalFineCollected || 0)}</p>
                </div>
                <div className="rounded-lg bg-slate-50 p-3">
                  <p className="text-slate-500">Avg Fine</p>
                  <p className="text-xl font-semibold">Rs {Math.round(stats.averageFine || 0)}</p>
                </div>
                <div className="rounded-lg bg-slate-50 p-3">
                  <p className="text-slate-500">Max Excess Speed</p>
                  <p className="text-xl font-semibold">{(stats.maxExcessSpeed || 0).toFixed(1)} km/h</p>
                </div>
              </div>
            )}
          </section>
        </div>

        <section className="mt-6 rounded-2xl bg-white p-5 shadow-sm ring-1 ring-slate-200">
          <div className="mb-4 flex items-center justify-between">
            <h2 className="text-lg font-semibold">Violation Records</h2>
            <button
              type="button"
              onClick={loadViolations}
              className="rounded-lg border border-slate-300 px-3 py-1.5 text-sm hover:bg-slate-50"
            >
              Refresh
            </button>
          </div>

          {error && <p className="mb-3 rounded-md bg-red-50 px-3 py-2 text-sm text-red-700">{error}</p>}

          {loadingList ? (
            <p className="text-sm text-slate-500">Loading violations...</p>
          ) : violations.length === 0 ? (
            <p className="text-sm text-slate-500">No violations found.</p>
          ) : (
            <div className="overflow-x-auto">
              <table className="min-w-full text-left text-sm">
                <thead className="border-b border-slate-200 text-slate-600">
                  <tr>
                    <th className="py-2 pr-4">Plate</th>
                    <th className="py-2 pr-4">Excess Speed</th>
                    <th className="py-2 pr-4">Fine</th>
                    <th className="py-2 pr-4">Location</th>
                    <th className="py-2 pr-4">Created At</th>
                  </tr>
                </thead>
                <tbody>
                  {violations.map((v) => (
                    <tr key={v.id} className="border-b border-slate-100">
                      <td className="py-2 pr-4 font-medium">{v.licensePlate}</td>
                      <td className="py-2 pr-4">{Number(v.excessSpeed || 0).toFixed(1)} km/h</td>
                      <td className="py-2 pr-4">Rs {Math.round(v.fineAmount || 0)}</td>
                      <td className="py-2 pr-4">{v.location}</td>
                      <td className="py-2 pr-4">{v.createdAt ? new Date(v.createdAt).toLocaleString() : '-'}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </section>
      </section>
    </main>
  )
}

export default App
