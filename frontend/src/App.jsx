import { useEffect, useState } from 'react'

function App() {
  const [backendStatus, setBackendStatus] = useState('Checking backend...')

  useEffect(() => {
    fetch('http://localhost:8080/api/health')
      .then((response) => response.json())
      .then((data) => {
        setBackendStatus(`${data.status} - ${data.message}`)
      })
      .catch(() => {
        setBackendStatus('Backend connection failed')
      })
  }, [])

  return (
    <div>
      <h1>DentCare</h1>
      <h2>Backend Status</h2>
      <p>{backendStatus}</p>
    </div>
  )
}

export default App