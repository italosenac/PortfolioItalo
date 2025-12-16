import { useMemo, useState, type JSX } from 'react'
import { useNavigate } from 'react-router-dom'
import LoadingOverlay from './LoadingOverlay'
import '@fontsource/comfortaa/400.css'
import VoltarBotao from './VoltarBotao'
import { MapContainer, TileLayer, Marker, Popup, Circle } from 'react-leaflet'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'

type Prestador = {
  nome: string
  tempoExp: number
  qtdAvaliacoes: number
  nota: number
  especialidades: string[]
  imagem: string
  location: { lat: number; lng: number }
}

const prestadores: Prestador[] = [
  { tempoExp: 1, qtdAvaliacoes: 117, nome: 'Ana Cabeleireira', nota: 4.9, especialidades: ['Corte', 'Coloração'], imagem: 'https://img.freepik.com/fotos-premium/cabeleireira-de-mulher-muito-madura-trabalhando-em-um-salao-de-beleza-e-secando-o-cabelo-da-cliente_328764-1247.jpg', location: { lat: -8.0572, lng: -34.8821 } },
  { tempoExp: 3, qtdAvaliacoes: 11, nome: 'Carlos Barbeiro', nota: 1.7, especialidades: ['Barba', 'Corte Masculino'], imagem: 'https://img.freepik.com/fotos-premium/barbeiro-profissional-homem-segurar-barbeadora-retro-bonito-cabeleireiro-com-corte-de-cabelo-barbeiro-homem-cabeleireiros-e-barbeiros-barbeiro-em-barbearia-isolado-em-preto-barbeiro-homem-estilo-de-cabelo_474717-159122.jpg', location: { lat: -8.0558, lng: -34.8835 } },
  { tempoExp: 2, qtdAvaliacoes: 96, nome: 'Mariana Manicure', nota: 2.8, especialidades: ['Unhas', 'Alongamento'], imagem: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTLcegHaGEA65h6jYm91_5tWOtdaZfmH0cuMw&s', location: { lat: -8.0586, lng: -34.8808 } },
  { tempoExp: 1, qtdAvaliacoes: 43, nome: 'Lucas Maquiador', nota: 0.6, especialidades: ['Maquiagem Social', 'Noiva'], imagem: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR2NXYrrZjBZPpna6LygzjmzAX3fqOUbtEVaA&s', location: { lat: -8.0600, lng: -34.8840 } },
  { tempoExp: 6, qtdAvaliacoes: 417, nome: 'Sofia Esteticista', nota: 4.4, especialidades: ['Limpeza de Pele', 'Tratamento Facial'], imagem: 'https://blog.extratosdaterra.com.br/wp-content/uploads/2021/08/Capa_Blog_O-que-faz-um-esteticista_4852021-1024x576.png', location: { lat: -8.0565, lng: -34.8815 } },
  { tempoExp: 5, qtdAvaliacoes: 57, nome: 'Paula Depiladora', nota: 4.5, especialidades: ['Depilação a Cera', 'Laser'], imagem: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ-mfL-FQfh0_Wdhmb2hYVQ1f2Ya-1GNn8Vyw&s', location: { lat: -8.0549, lng: -34.8852 } },
  { tempoExp: 3, qtdAvaliacoes: 341, nome: 'Juliana Massoterapeuta', nota: 3.8, especialidades: ['Massagem Relaxante', 'Drenagem'], imagem: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSTvLN2OuRWiKrmL6wVDNuY1JU1JFdwJElSvg&s', location: { lat: -8.0589, lng: -34.8860 } },
  { tempoExp: 12, qtdAvaliacoes: 1117, nome: 'Rafaela Designer', nota: 4.9, especialidades: ['Sobrancelhas', 'Micropigmentação'], imagem: 'https://img.freepik.com/fotos-premium/designer-de-sobrancelhas-empresaria-profissional-esteticista-mulher-segurando-pincas-e-ferramentas-para-designer-de-sobrancelhas_579344-953.jpg', location: { lat: -8.0597, lng: -34.8799 } },
  { tempoExp: 9, qtdAvaliacoes: 969, nome: 'Camila Bronzeadora', nota: 4.6, especialidades: ['Bronzeamento Natural', 'Spray'], imagem: 'https://roteirodabeleza.com/wp-content/uploads/2024/11/personal-bronze.png', location: { lat: -8.0612, lng: -34.8832 } }
]

const MARKER_ICON = L.icon({
  iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
  iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [0, -36]
})
L.Marker.prototype.options.icon = MARKER_ICON

const MAP_TILE_URL = 'https://tiles.stadiamaps.com/tiles/alidade_smooth/{z}/{x}/{y}.png'

function useCentroMapa(lista: Prestador[]) {
  return useMemo(() => {
    if (!lista || lista.length === 0) return { lat: -8.057, lng: -34.882 }
    const soma = lista.reduce((acc, p) => {
      acc.lat += p.location.lat
      acc.lng += p.location.lng
      return acc
    }, { lat: 0, lng: 0 })
    return { lat: soma.lat / lista.length, lng: soma.lng / lista.length }
  }, [lista])
}

function calcularDistanciaKm(a: { lat: number; lng: number }, b: { lat: number; lng: number }) {
  const toRad = (v: number) => (v * Math.PI) / 180
  const R = 6371
  const dLat = toRad(b.lat - a.lat)
  const dLon = toRad(b.lng - a.lng)
  const lat1 = toRad(a.lat)
  const lat2 = toRad(b.lat)
  const sinDLat = Math.sin(dLat / 2)
  const sinDLon = Math.sin(dLon / 2)
  const aHarv = sinDLat * sinDLat + Math.cos(lat1) * Math.cos(lat2) * sinDLon * sinDLon
  const c = 2 * Math.atan2(Math.sqrt(aHarv), Math.sqrt(1 - aHarv))
  return R * c
}

export default function BelezaEstetica(): JSX.Element {
  const navigate = useNavigate()

  const [especialidadeSelecionada, setEspecialidadeSelecionada] = useState('')
  const [notaMinima, setNotaMinima] = useState('')
  const [anosExperienciaMin, setAnosExperienciaMin] = useState('')
  const [loading, setLoading] = useState(false)

  const [localUsuario, setLocalUsuario] = useState<{ lat: number; lng: number } | null>(null)
  const [raioKm, setRaioKm] = useState<number>(20)

  function obterLocalizacaoUsuario() {
    if (!navigator.geolocation) {
      alert('Geolocalização não disponível neste navegador')
      return
    }
    navigator.geolocation.getCurrentPosition(
      pos => setLocalUsuario({ lat: pos.coords.latitude, lng: pos.coords.longitude }),
      err => {
        console.warn('Erro ao obter localização', err)
        alert('Não foi possível obter sua localização. Permita o acesso à localização e tente novamente.')
      },
      { enableHighAccuracy: true, timeout: 8000 }
    )
  }

  function filtrarPrestador(p: Prestador) {
    const passouEspecialidade = especialidadeSelecionada === '' || p.especialidades.includes(especialidadeSelecionada)
    const passouExperiencia = anosExperienciaMin === '' || p.tempoExp >= parseInt(anosExperienciaMin || '0')
    const passouNota = notaMinima === '' || p.nota >= parseFloat(notaMinima || '0')

    if (!localUsuario || raioKm <= 0) return passouEspecialidade && passouExperiencia && passouNota

    const distancia = calcularDistanciaKm(localUsuario, p.location)
    return passouEspecialidade && passouExperiencia && passouNota && distancia <= raioKm
  }

  const prestadoresFiltrados = useMemo(() => prestadores.filter(filtrarPrestador), [especialidadeSelecionada, anosExperienciaMin, notaMinima, localUsuario, raioKm])
  const centroMapa = localUsuario ?? useCentroMapa(prestadoresFiltrados)

  function aoClicarSolicitar() {
    setLoading(true)
    setTimeout(() => navigate('/login'), 300)
  }

  const mapaKey = `${centroMapa.lat}-${centroMapa.lng}-${raioKm}-${prestadoresFiltrados.length}`

  return (
    <div className="bodyEstetica" style={{ fontFamily: 'Comfortaa', minHeight: '100vh' }}>
      <style>{`
        /* Forçar mapa visível acima do background e dar fallback de cor enquanto tiles carregam */
        .locals-map-wrapper { position: relative; z-index: 1; height: 420px; border-radius: 12px; overflow: hidden; box-shadow: 0 6px 18px rgba(0,0,0,0.12); background: #e6e6e6; }
        .leaflet-container { border-radius: 12px; min-height: 100%; background: #e6e6e6; }
        .locals-solicitar-button { font-family: 'Comfortaa'; padding: 9px 12px; border-radius: 8px; border: none; cursor: pointer; background: #722652ff; color: #fff; font-weight: 700; box-shadow: 0 6px 14px rgba(0,0,0,0.18); height: 36px; }
        @media (max-width:720px){ .locals-map-wrapper{height:320px} }
      `}</style>

      <VoltarBotao />
      {loading && <LoadingOverlay />}

      <div className="container locals-page-center" style={{ paddingBottom: 20 }}>
        <h1 className="titulo" style={{ textAlign: 'center', marginTop: 14, fontSize: '2.6rem', letterSpacing: 6 }}>LOCALS</h1>
        <h2 style={{ textAlign: 'center', marginBottom: 8 }}>BELEZA E ESTÉTICA</h2>

        <div style={{ display: 'flex', gap: 12, alignItems: 'center', margin: '12px 0', flexWrap: 'wrap' }}>
          <select value={especialidadeSelecionada} onChange={e => setEspecialidadeSelecionada(e.target.value)} style={{ fontFamily: 'Comfortaa' }}>
            <option value="">Todas as especialidades</option>
            <option value="Corte">Corte</option>
            <option value="Coloração">Coloração</option>
            <option value="Barba">Barba</option>
            <option value="Corte Masculino">Corte Masculino</option>
            <option value="Unhas">Unhas</option>
            <option value="Alongamento">Alongamento</option>
            <option value="Maquiagem Social">Maquiagem Social</option>
            <option value="Noiva">Noiva</option>
            <option value="Limpeza de Pele">Limpeza de Pele</option>
            <option value="Tratamento Facial">Tratamento Facial</option>
            <option value="Depilação a Cera">Depilação a Cera</option>
            <option value="Laser">Laser</option>
            <option value="Massagem Relaxante">Massagem Relaxante</option>
            <option value="Drenagem">Drenagem</option>
            <option value="Sobrancelhas">Sobrancelhas</option>
            <option value="Micropigmentação">Micropigmentação</option>
            <option value="Bronzeamento Natural">Bronzeamento Natural</option>
            <option value="Spray">Spray</option>
          </select>

          <select value={notaMinima} onChange={e => setNotaMinima(e.target.value)} style={{ fontFamily: 'Comfortaa' }}>
            <option value="">Todas as notas</option>
            <option value="4">Acima de 4</option>
            <option value="4.5">Acima de 4.5</option>
          </select>

          <select value={anosExperienciaMin} onChange={e => setAnosExperienciaMin(e.target.value)} style={{ fontFamily: 'Comfortaa' }}>
            <option value="">Tempo de experiência</option>
            <option value="1">Acima de 1 ano</option>
            <option value="2">Acima de 2 anos</option>
            <option value="3">Acima de 3 anos</option>
            <option value="4">Acima de 4 anos</option>
          </select>

          <div style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
            <select value={String(raioKm)} onChange={e => setRaioKm(parseInt(e.target.value))} style={{ fontFamily: 'Comfortaa' }}>
              <option value="0">Todas as distâncias</option>
              <option value="5">Até 5 km</option>
              <option value="10">Até 10 km</option>
              <option value="20">Até 20 km</option>
              <option value="50">Até 50 km</option>
              <option value="100">Até 100 km</option>
            </select>
        </div>
          <div style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
            <button onClick={obterLocalizacaoUsuario}>Usar minha localização</button>
          </div>

          <div style={{ marginLeft: 'auto', fontSize: 14 }}>{prestadoresFiltrados.length} prestador(es) no mapa</div>
        </div>

        <div className="locals-map-wrapper">
          {}
          <MapContainer key={mapaKey} center={[centroMapa.lat, centroMapa.lng]} zoom={14} style={{ height: '100%', width: '100%' }}>
            <TileLayer attribution="&copy; OpenStreetMap" url={MAP_TILE_URL} />

            {localUsuario && raioKm > 0 && (
              <Circle center={[localUsuario.lat, localUsuario.lng]} radius={raioKm * 1000} pathOptions={{ fillOpacity: 0.06, color: '#722652' }} />
            )}

            {prestadoresFiltrados.map((p, index) => (
              <Marker key={index} position={[p.location.lat, p.location.lng]}>
                <Popup minWidth={240}>
                  <div style={{ fontFamily: 'Comfortaa' }}>
                    <div style={{ display: 'flex', gap: 10 }}>
                      <img src={p.imagem} alt={p.nome} style={{ width: 80, height: 60, objectFit: 'cover', borderRadius: 8 }} />
                      <div style={{ width: '100%' }}>
                        <strong style={{ fontSize: 16 }}>{p.nome}</strong>
                        <div style={{ fontSize: 13, marginTop: 4 }}>{p.especialidades.join(' • ')}</div>
                        <div style={{ fontSize: 13, marginTop: 6 }}>Nota: <strong>{p.nota.toFixed(1)}</strong> • {p.tempoExp} ano(s)</div>
                        {localUsuario && (
                          <div style={{ fontSize: 12, marginTop: 6 }}>Distância: <strong>{calcularDistanciaKm(localUsuario, p.location).toFixed(2)} km</strong></div>
                        )}
                        <div style={{ marginTop: 8}}>
                          <button className="locals-solicitar-button locals-solicitar-button--popup" onClick={aoClicarSolicitar}>Solicitar / Entrar</button>
                        </div>
                      </div>
                    </div>
                  </div>
                </Popup>
              </Marker>
            ))}
          </MapContainer>
        </div>
      </div>
    </div>
  )
}
