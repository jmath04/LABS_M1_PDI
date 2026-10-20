# Relatório Técnico - Laboratórios M2.1 e M2.2

**Autor:** João Matheus de Oliveira Vieira  
**Disciplina:** Processamento de Imagens Digitais (PDI)  
**Ano/Semestre:** 2026-02

---

## Módulo 2.1 - Máscara Binária e Segmentação

### 1. Definição do Alvo e Imagens de Entrada
**Objetivo:** Isolar [descreva o objeto, ex: moedas, grãos, peças] do fundo da imagem.
**Imagens utilizadas:**
* `favo_simples.jpg`: Condições favoráveis de iluminação e contraste.
* `favo_dificil.jpg`: Apresenta dificuldades reais como [descreva o problema, ex: sombras fortes, fundo texturizado, reflexos], contendo múltiplos objetos sobrepostos.

### 2. Métodos e Configurações Testadas
Para avaliar a melhor extração, dois métodos de limiarização do OpenCV foram aplicados sobre a imagem em tons de cinza:
1. **Limiarização Fixa (Fixed):** Utilizado o valor de corte T = [insira seu valor, ex: 120].
2. **Limiarização de Otsu (Otsu):** Limiar calculado automaticamente pelo algoritmo baseado no histograma da imagem.

*(Nota: Se foi necessário aplicar inversão porque o objeto era escuro, mencione aqui que utilizou a inversão da imagem antes ou durante a limiarização).*

### 3. Análise dos Resultados e Escolha da Máscara

| Imagem | Método | Pixels de Objeto (255) | Proporção do Primeiro Plano |
| :--- | :--- | :--- | :--- |
| Favorável | Fixed | [valor] | [%] |
| Favorável | Otsu | [valor] | [%] |
| Difícil | Fixed | [valor] | [%] |
| Difícil | Otsu | [valor] | [%] |

**Análise Visual:**
* **Falhas do Limiar Fixo:** No caso difícil, o limiar fixo [descreva: perdeu partes do objeto na sombra / incluiu muito ruído do fundo].
* **Comportamento do Otsu:** O método de Otsu [descreva: conseguiu adaptar melhor a separação / falhou ao dividir a imagem em duas massas irreais].

**Configuração Escolhida para o M2.2:**
A máscara gerada pelo método **[Fixed ou Otsu]** na imagem difícil será utilizada na próxima etapa, pois [justifique, ex: preservou melhor a forma estrutural dos objetos, mesmo deixando ruídos que podem ser limpos por morfologia]. Os erros atuais que podem atrapalhar a contagem são [ex: pequenos pontos brancos isolados (falsos positivos) e buracos pretos dentro dos objetos].

---

## Módulo 2.2 - Componentes Conexos e Sequência Morfológica

### 1. Rotulação Inicial e Conectividade
A máscara bruta (escolhida no M2.1) foi submetida à contagem de componentes conexos antes de qualquer limpeza.

* **Conectividade 4:** [quantidade] componentes detetados.
* **Conectividade 8:** [quantidade] componentes detetados.

**Diferença Observada:** A conectividade 4 considera vizinhos apenas na horizontal/vertical, fazendo com que pixels unidos apenas pelas diagonais sejam contados como objetos separados. A conectividade 8 unifica essas conexões diagonais, resultando numa contagem [maior/menor/igual], que se aproxima mais da contagem visual real. Para a análise final, foi adotada a **conectividade 8**.

### 2. Sequência Morfológica Aplicada
Para corrigir os defeitos da máscara inicial, aplicou-se a seguinte sequência utilizando um elemento estruturante retangular de 5x5, com 1 iteração por estágio:

1. **Abertura (Open):**
    * **Justificativa:** Aplicada primeiro para eliminar pequenos ruídos brancos (falsos positivos) espalhados pelo fundo preto, que inflavam artificialmente a contagem de objetos.
2. **Fechamento (Close):**
    * **Justificativa:** Aplicada em seguida para fechar os pequenos buracos pretos (falsos negativos) que se formaram dentro das áreas brancas dos objetos principais.

### 3. Recontagem e Comparações
Filtro de área mínima aplicado: --min-area [ex: 50] pixels.

| Métrica | Máscara Inicial (Bruta) | Máscara Final (Pós-Morfologia e Filtro) |
| :--- | :--- | :--- |
| **Área Total (Pixels Brancos)** | [valor inicial] | [valor final] |
| **Contagem de Componentes** | [contagem inicial 8] | [contagem filtrada] |

**Observações sobre as mudanças:**
* A área total de pixels [aumentou/diminuiu] levemente devido [à remoção de ruídos na abertura / preenchimento de buracos no fechamento].
* A contagem de componentes caiu drasticamente de [X] para [Y], eliminando o lixo visual e isolando as estruturas de interesse.

### 4. Limitações Críticas
* **Onde a sequência ajuda:** Em imagens com muito ruído de fundo isolado ou falhas internas no preenchimento do objeto, a Abertura seguida de Fechamento regulariza as bordas perfeitamente e estabiliza o centroide (CSV).
* **Onde a sequência falha/prejudica:** Em casos onde os objetos originais estão quase a tocar-se (contacto de regiões), a operação de Fechamento ou a dilatação embutida nas operações pode fundir dois objetos distintos num único componente. Nesses casos, a rotulação simples não equivale ao número real de objetos sem a aplicação de técnicas adicionais como Watershed.