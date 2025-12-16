import { useMemo, useState, type JSX } from 'react'
import { useNavigate } from 'react-router-dom'
import '@fontsource/comfortaa/400.css'
import VoltarBotao from './VoltarBotao'
import LoadingOverlay from './LoadingOverlay'
import { MapContainer, TileLayer, Marker, Popup, Circle } from 'react-leaflet'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'

type Prestador = {
  tempoExp: number
  nome: string
  qtdAvaliacoes: number
  nota: number
  especialidades: string[]
  imagem: string
  location: { lat: number; lng: number }
}

const MAP_TILE_URL = 'https://tiles.stadiamaps.com/tiles/alidade_smooth/{z}/{x}/{y}.png'

const MARKER_ICON = L.icon({
  iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
  iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [0, -36],
})
L.Marker.prototype.options.icon = MARKER_ICON

const PRESTADORES: Prestador[] = [
  {
    tempoExp: 8,
    qtdAvaliacoes: 75,
    nome: 'João da Pintura',
    nota: 4.5,
    especialidades: ['Pintura', 'Reboco'],
    imagem: 'https://www.shutterstock.com/image-photo/painter-doing-painting-work-on-260nw-2512467065.jpg',
    location: { lat: -8.057, lng: -34.882 },
  },
  {
    tempoExp: 4,
    qtdAvaliacoes: 32,
    nome: 'Maria Encanadora',
    nota: 4.8,
    especialidades: ['Hidráulica', 'Instalação de torneiras'],
    imagem:
      'https://img.freepik.com/fotos-gratis/jovem-construtora-com-uniforme-de-construcao-e-capacete-de-seguranca-mostrando-a-chave-inglesa-parecendo-confiante-em-pe-sobre-a-parede-laranja_141793-29098.jpg?w=740',
    location: { lat: -8.053, lng: -34.885 },
  },
  {
    tempoExp: 6,
    qtdAvaliacoes: 82,
    nome: 'Carlos Carpinteiro',
    nota: 4.3,
    especialidades: ['Madeira', 'Montagem'],
    imagem: 'https://www.shutterstock.com/image-photo/carpenter-sanding-wooden-plank-power-600nw-2662388269.jpg',
    location: { lat: -8.06, lng: -34.879 },
  },
  {
    tempoExp: 3,
    qtdAvaliacoes: 13,
    nome: 'Rafael Marceneiro',
    nota: 4.6,
    especialidades: ['Madeira', 'Montagem'],
    imagem: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ6Q5D7DaDnS5MI6acWIG8UXVKRi-j1XEKjnA',
    location: { lat: -8.063, lng: -34.887 },
  },
  {
    tempoExp: 7,
    qtdAvaliacoes: 8,
    nome: 'Laura da Hidráulica',
    nota: 4.4,
    especialidades: ['Hidráulica', 'Manutenção'],
    imagem: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTmJqtDROeug04QAOtCPJxHlOYYOqJluUfx2w',
    location: { lat: -8.0595, lng: -34.884 },
  },
  {
    tempoExp: 2,
    qtdAvaliacoes: 275,
    nome: 'Marcelo Pintor',
    nota: 1.2,
    especialidades: ['Pintura', 'Acabamento'],
    imagem: 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?auto=format&fit=crop&w=400&h=300',
    location: { lat: -8.055, lng: -34.88 },
  },
]

function matchesEspecialidade(prestador: Prestador, filtro: string) {
  if (!filtro) return true
  return prestador.especialidades.includes(filtro)
}

function meetsNotaMinima(prestador: Prestador, notaMin: string) {
  if (!notaMin) return true
  return prestador.nota >= parseFloat(notaMin)
}

function meetsExperienciaMinima(prestador: Prestador, experienciaMin: string) {
  if (!experienciaMin) return true
  return prestador.tempoExp >= parseInt(experienciaMin)
}

function useMapaCentro(prestadores: Prestador[]) {
  return useMemo(() => {
    if (prestadores.length === 0) return { lat: -8.057, lng: -34.882 }
    const soma = prestadores.reduce(
      (acc, p) => {
        acc.lat += p.location.lat
        acc.lng += p.location.lng
        return acc
      },
      { lat: 0, lng: 0 },
    )
    return { lat: soma.lat / prestadores.length, lng: soma.lng / prestadores.length }
  }, [prestadores])
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

export default function ConstrucaoCivil(): JSX.Element {
  const navigate = useNavigate()

  const [filtroEspecialidade, setFiltroEspecialidade] = useState('')
  const [filtroNotaMinima, setFiltroNotaMinima] = useState('')
  const [filtroExperienciaMinima, setFiltroExperienciaMinima] = useState('')

  const [localUsuario, setLocalUsuario] = useState<{ lat: number; lng: number } | null>(null)
  const [raioKm, setRaioKm] = useState<number>(20)

  const [estaCarregando, setEstaCarregando] = useState(false)

  function obterLocalizacaoUsuario() {
    if (!navigator.geolocation) {
      alert('Geolocalização não suportada')
      return
    }
    navigator.geolocation.getCurrentPosition(
      pos => setLocalUsuario({ lat: pos.coords.latitude, lng: pos.coords.longitude }),
      () => alert('Não foi possível obter a localização'),
      { enableHighAccuracy: true, timeout: 8000 }
    )
  }

  function filtrarPrestador(p: Prestador) {
    const passouEspecialidade = matchesEspecialidade(p, filtroEspecialidade)
    const passouNota = meetsNotaMinima(p, filtroNotaMinima)
    const passouExp = meetsExperienciaMinima(p, filtroExperienciaMinima)
    if (!localUsuario || raioKm <= 0) return passouEspecialidade && passouNota && passouExp
    const distancia = calcularDistanciaKm(localUsuario, p.location)
    return passouEspecialidade && passouNota && passouExp && distancia <= raioKm
  }

  const prestadoresFiltrados = useMemo(
    () => PRESTADORES.filter(filtrarPrestador),
    [filtroEspecialidade, filtroNotaMinima, filtroExperienciaMinima, localUsuario, raioKm],
  )

  const centroMapa = localUsuario ?? useMapaCentro(prestadoresFiltrados)

  function abrirTelaLogin() {
    setEstaCarregando(true)
    setTimeout(() => navigate('/login'), 300)
  }

  const mapaKey = `${centroMapa.lat}-${centroMapa.lng}-${raioKm}-${prestadoresFiltrados.length}`

  return (
    <div className="bodyConstrucao" style={{ fontFamily: 'Comfortaa' }}>
      <style>{`
        .locals-map-wrapper { position: relative; z-index: 1; height: 420px; border-radius: 12px; overflow: hidden; box-shadow: 0 6px 18px rgba(0,0,0,0.12); background: #d9d9d9 }
        .leaflet-container { border-radius: 12px; min-height: 100%; background: #d9d9d9; }
        .leaflet-popup-tip { display:none !important }
        .locals-solicitar-button { font-family: 'Comfortaa'; padding: 9px 12px; border-radius: 10px; border: none; cursor: pointer; background: #8c4614; color: #fff; font-weight: 700; box-shadow: 0 6px 14px rgba(0,0,0,0.18); height: 36px }
        @media(max-width:720px){ .locals-map-wrapper{height:320px} }
      `}</style>

      <VoltarBotao />
      {estaCarregando && <LoadingOverlay />}

      <div className="container locals-page-center" style={{ paddingBottom: 20 }}>
        <h1 className="titulo" style={{ textAlign: 'center', marginTop: 14, fontSize: '2.6rem', letterSpacing: 6 }}>LOCALS</h1>
        <h2 style={{ textAlign: 'center', marginBottom: 8 }}>CONSTRUÇÃO CIVIL</h2>

        <div style={{ display: 'flex', gap: 12, alignItems: 'center', margin: '12px 0', flexWrap: 'wrap' }}>
          <select value={filtroEspecialidade} onChange={e => setFiltroEspecialidade(e.target.value)} style={{ fontFamily: 'Comfortaa' }}>
            <option value="">Todas as especialidades</option>
            <option value="Pintura">Pintura</option>
            <option value="Reboco">Reboco</option>
            <option value="Hidráulica">Hidráulica</option>
            <option value="Instalação de torneiras">Instalação de torneiras</option>
            <option value="Madeira">Madeira</option>
            <option value="Montagem">Montagem</option>
            <option value="Instalação elétrica">Instalação elétrica</option>
            <option value="Manutenção elétrica">Manutenção elétrica</option>
            <option value="Alvenaria">Alvenaria</option>
            <option value="Decoração">Decoração</option>
            <option value="Manutenção">Manutenção</option>
            <option value="Acabamento">Acabamento</option>
          </select>

          <select value={filtroNotaMinima} onChange={e => setFiltroNotaMinima(e.target.value)} style={{ fontFamily: 'Comfortaa' }}>
            <option value="">Todas as notas</option>
            <option value="1">Acima de 1</option>
            <option value="2">Acima de 2</option>
            <option value="3">Acima de 3</option>
            <option value="4">Acima de 4</option>
            <option value="4.5">Acima de 4.5</option>
          </select>

          <select value={filtroExperienciaMinima} onChange={e => setFiltroExperienciaMinima(e.target.value)} style={{ fontFamily: 'Comfortaa' }}>
            <option value="">Tempo de experiência</option>
            <option value="1">Acima de 1 ano</option>
            <option value="2">Acima de 2 anos</option>
            <option value="3">Acima de 3 anos</option>
            <option value="4">Acima de 4 anos</option>
          </select>

          <div style={{ display: 'flex', gap: 10, alignItems: 'center' }}>
            <select
              value={String(raioKm)}
              onChange={e => setRaioKm(parseInt(e.target.value))}
              style={{ fontFamily: 'Comfortaa', height: 36, padding: '6px 10px' }}
            >
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

          <div style={{ marginLeft: 'auto', fontSize: 14 }}>
            {prestadoresFiltrados.length} prestador(es) no mapa
          </div>
        </div>

        <div className="locals-map-wrapper">
          <MapContainer
            key={mapaKey}
            center={[centroMapa.lat, centroMapa.lng]}
            zoom={14}
            style={{ height: '100%', width: '100%' }}
          >
            <TileLayer attribution="&copy; OpenStreetMap" url={MAP_TILE_URL} />

            {localUsuario && raioKm > 0 && (
              <Circle
                center={[localUsuario.lat, localUsuario.lng]}
                radius={raioKm * 1000}
                pathOptions={{ fillOpacity: 0.06, color: '#8c4614' }}
              />
            )}

            {prestadoresFiltrados.map((prestador, index) => (
              <Marker key={index} position={[prestador.location.lat, prestador.location.lng]}>
                <Popup minWidth={240}>
                  <div style={{ fontFamily: 'Comfortaa' }}>
                    <div style={{ display: 'flex', gap: 10 }}>
                      <img src={prestador.imagem} alt={prestador.nome} style={{ width: 80, height: 60, objectFit: 'cover', borderRadius: 8 }} />
                      <div style={{ width: '100%' }}>
                        <strong style={{ fontSize: 16 }}>{prestador.nome}</strong>
                        <div style={{ fontSize: 13, marginTop: 4 }}>{prestador.especialidades.join(' • ')}</div>
                        <div style={{ fontSize: 13, marginTop: 6 }}>
                          Nota: <strong>{prestador.nota.toFixed(1)}</strong> • {prestador.tempoExp} anos
                        </div>
                        {localUsuario && (
                          <div style={{ fontSize: 12, marginTop: 6 }}>
                            Distância: <strong>{calcularDistanciaKm(localUsuario, prestador.location).toFixed(2)} km</strong>
                          </div>
                        )}
                        <div style={{ marginTop: 8 }}>
                          <button className="locals-solicitar-button locals-solicitar-button--popup" onClick={abrirTelaLogin}>
                            Solicitar / Entrar
                          </button>
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
