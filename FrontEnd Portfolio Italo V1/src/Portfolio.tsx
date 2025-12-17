import React, { useEffect, useRef, useState, type JSX } from 'react'
import styled from 'styled-components'

type Imagem = {
  titulo: string
  url: string
  link: string
}

const IMAGENS: Imagem[] = [
   {
    titulo: 'LOCALS',
    url: 'Selecao_Construcao_Civil_Desktop.png'
    ,link: 'https://github.com/italosenac/PortfolioItalo/tree/master/LOCALS%20-%20PrototipoV1'  
},
      {
    titulo: 'PET SHOp',
    url: 'petshop.jpeg'
    ,link: 'https://github.com/italosenac/PortfolioItalo/tree/5d880e931cad631e9787365afd86697c68053695/5%20Sistemas%20-%20Avalia%C3%A7%C3%A3o%20Thalyta%20-%20Italo%20R%20Pereira/sistemaPetShop'    
},
  {
    titulo: 'vENDaS',
    url: 'vendas.jpeg',
    link: 'https://github.com/italosenac/PortfolioItalo/tree/5d880e931cad631e9787365afd86697c68053695/5%20Sistemas%20-%20Avalia%C3%A7%C3%A3o%20Thalyta%20-%20Italo%20R%20Pereira'
    
},
 
  {
    titulo: 'bIbLIOTECA',
    url: 'biblioteca.jpeg',
    link: 'https://github.com/italosenac/PortfolioItalo/tree/5d880e931cad631e9787365afd86697c68053695/5%20Sistemas%20-%20Avalia%C3%A7%C3%A3o%20Thalyta%20-%20Italo%20R%20Pereira'    
},

  {
    titulo: 'cLINICAS',
    url: 'clinicas.jpeg',
    link: 'https://github.com/italosenac/PortfolioItalo/tree/5d880e931cad631e9787365afd86697c68053695/5%20Sistemas%20-%20Avalia%C3%A7%C3%A3o%20Thalyta%20-%20Italo%20R%20Pereira'  

    },
  
  {
    titulo: 'CHAT agENT',
    url: 'chatagent.jpeg'
    ,link: 'https://github.com/italosenac/PortfolioItalo/tree/5d880e931cad631e9787365afd86697c68053695/5%20Sistemas%20-%20Avalia%C3%A7%C3%A3o%20Thalyta%20-%20Italo%20R%20Pereira'  
}
 
]

const INTERVALO_MS = 5000

export default function Portfolio(): JSX.Element {
  const [indice, setIndice] = useState<number>(0)
  const timerRef = useRef<number | null>(null)
  const total = IMAGENS.length

  useEffect(() => {
    iniciarTimer()
    return pararTimer
  }, [])

  function iniciarTimer(): void {
    pararTimer()
    timerRef.current = window.setInterval(() => avancarAutomatico(), INTERVALO_MS)
  }

  function pararTimer(): void {
    if (timerRef.current !== null) {
      clearInterval(timerRef.current)
      timerRef.current = null
    }
  }

  function avancarAutomatico(): void {
    setIndice((atual) => (atual + 1) % total)
  }

  function avancarManual(): void {
    setIndice((atual) => (atual + 1) % total)
    reiniciarTimer()
  }

  function retrocederManual(): void {
    setIndice((atual) => (atual - 1 + total) % total)
    reiniciarTimer()
  }

  function irParaSlide(posicao: number): void {
    setIndice(posicao)
    reiniciarTimer()
  }

  function reiniciarTimer(): void {
    pararTimer()
    iniciarTimer()
  }

  return (
    
    <TelaInteira>
        <div className='headerLogoItalo'>Ítalo Rodrigues
         <div className="iconesRight">
        <div className="github">
          <a href = "https://github.com/italosenac" className="github">
            <img src="src\assets\GitHub-Logos\GitHub_Logo_White.png" alt="Logo GitHub"/>
            <img src="/src\assets\github-mark\github-mark-white.png" alt="Logo GitHub" />
          </a>
        </div>
            </div>
            <div className="spinner">
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
            </div>
        </div>
      <Slider>
        {IMAGENS.map((item, i) => (
          <Slide key={i} visivel={i === indice} imagem={item.url}>
            <Legenda onClick={() => window.open(item.link, '_blank')}>
                {item.titulo}
            </Legenda>
          </Slide>
        ))}

        <Controles>
          <button onClick={retrocederManual} aria-label="anterior">◀</button>
          <button onClick={avancarManual} aria-label="próximo">▶</button>
        </Controles>

        <Indicadores>
          {IMAGENS.map((_, i) => (
            <button
              key={i}
              className={i === indice ? 'ativo' : ''}
              onClick={() => irParaSlide(i)}
              aria-label={`ir para slide ${i + 1}`}
            />
          ))}
        </Indicadores>
      </Slider>
    </TelaInteira>
  )
}

const TelaInteira = styled.div`
position: fixed;
inset: 0;
width: 100vw;
height: 100vh;
overflow: hidden;
font-family: 'Vostela';
font-size: 60px;

  
  
`

const Slider = styled.div`
  width: 100%;
  height: 100%;
  position: relative;
  background: #000;
`

const Slide = styled.div<{ visivel: boolean; imagem: string }>`
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  background-image: url(${(p) => p.imagem});
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: opacity 800ms ease, transform 800ms ease;
  opacity: ${(p) => (p.visivel ? 1 : 0)};
  transform: translateX(${(p) => (p.visivel ? '0%' : '5%')});
  
`

const Legenda = styled.button`
  color: white;
  font-size: clamp(20px, 4vw, 48px);
  letter-spacing: 2px;
  margin: 0;
  padding: 0;
  background: transparent;
  border: none;
  text-shadow: 1px 1px 2px black;
  cursor: pointer;
  font-family: 'Vostela';
font-weight: heavy;
  outline: none;

  &:focus {
    outline: none;
  }
`;


const Controles = styled.div`
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  bottom: 28px;
  display: flex;
  align-items: center;
  gap: 12px;
  background: rgba(0,0,0,0.18);
  padding: 6px 10px;
  border-radius: 999px;
  backdrop-filter: blur(4px);

  button {
    border: none;
    background: transparent;
    color: white;
    font-size: 18px;
    padding: 8px;
    cursor: pointer;
  }
`

const Indicadores = styled.div`
  position: absolute;
  right: 24px;
  top: 24px;
  display: flex;
  gap: 8px;

  button {
    width: 12px;
    height: 12px;
    border-radius: 50%;
    border: 1px solid rgba(255,255,255,0.6);
    background: transparent;
    cursor: pointer;
    padding: 0;
  }

  button.ativo {
    background: white;
  }
`
