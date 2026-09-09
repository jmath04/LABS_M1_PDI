# Processamento de Imagens — Laboratórios M1 (M1.1, M1.2 e M1.3)

**Autor:** João Matheus de Oliveira Vieira  
**Curso:** Ciência da Computação  
**Disciplina:** Processamento de Imagens Digitais (PDI)  
**Ano/Semestre:** 2026-02  

---

## Sobre o Projeto

Este projeto unifica as atividades dos laboratórios da M1 em uma única aplicação de linha de comando (**CLI**) desenvolvida em Java. O sistema foi construído com percurso explícito de pixels e implementação manual de algoritmos de matrizes (convolução), cumprindo estritamente as regras de **não utilização de bibliotecas prontas** para processamento de imagens (como OpenCV ou funções nativas de filtros).

A ferramenta é dividida em três frentes principais:
1. **M1.1:** Inspeção estrutural, metadados e contagem estatística de pixels.
2. **M1.2:** Transformações pontuais de intensidade (brilho, contraste, negativo, limiarização e histograma manual).
3. **M1.3:** Filtragem espacial e convolução por vizinhança (suavização, Laplaciano e detectores de borda Sobel).

---

## Pré-requisitos

Para compilar e executar este projeto, você precisará ter instalado em sua máquina:
- **Java Development Kit (JDK):** Versão 11 ou superior.
- **Apache Maven:** Para o gerenciamento de dependências e empacotamento.


## Como Compilar

Na raiz do projeto (onde está localizado o arquivo `pom.xml`), execute o seguinte comando no terminal para limpar compilações antigas e gerar o pacote executável (`.jar`):

```bash
mvn clean package
```

O arquivo executável final será gerado no diretório `target/pdi-lab.jar`.

---

## Como Executar (CLI)

A execução padrão segue a estrutura abaixo, informando o arquivo de entrada (`--input`) e a operação desejada (`--operation`):

```bash
java -jar target/pdi-lab.jar --input <caminho_imagem> --operation <nome_operacao> [argumentos_opcionais]

```

### Argumentos Disponíveis:

* `--input <path>`: *(Obrigatório)* Caminho da imagem de entrada (ex: `images/input/Kurama.jpg`).
* `--operation <nome>`: *(Obrigatório)* Nome da operação que deseja executar.
* `--output <path>`: *(Opcional)* Caminho onde a imagem de saída será salva. Caso omitido, o sistema gera um arquivo padrão na pasta `images/output/`.
* `--value <num>`: *(Opcional)* Valor numérico de parâmetro (usado em brilho, contraste e limiar).
* `--filtro <tipo>`: *(Opcional)* Escolha do kernel para suavização: `media3`, `ponderada3` ou `media5`. *(Padrão: media3)*
* `--borda <estratégia>`: *(Opcional)* Tratamento de borda na convolução: `copiar` ou `replicar`. *(Padrão: replicar)*

---

## Lista de Operações por Laboratório

### Módulo 1.1: Inspeção e Estrutura

* **Inspecionar metadados e pixels:**
```bash
java -jar target/pdi-lab.jar --input images/input/Kurama.jpg --operation inspect

```


* **Copiar imagem (pixel a pixel):**
```bash
java -jar target/pdi-lab.jar --input images/input/Kurama.jpg --output images/output/copia.jpg --operation copy

```
### Módulo 1.2: Transformações Pontuais
* **Ajuste de Brilho** (Ex: somar 50 à intensidade):
```bash
java -jar target/pdi-lab.jar --input images/input/Kurama.jpg --output images/output/brilho.jpg --operation brilho --value 50
```
* **Ajuste de Contraste** (Ex: fator $\alpha = 1.5$):
```bash
java -jar target/pdi-lab.jar --input images/input/Kurama.jpg --output images/output/contraste.jpg --operation contraste --value 1.5
```
* **Inverter Negativo:**
```bash
java -jar target/pdi-lab.jar --input images/input/Kurama.jpg --output images/output/negativo.jpg --operation negativo
```
* **Limiarização Binária** (Ex: Threshold $T = 128$):
```bash
java -jar target/pdi-lab.jar --input images/input/Kurama.jpg --output images/output/limiar.jpg --operation limiarizacao --value 128
```
* **Gerar Histograma Manual** (Gera o arquivo `images/output/histograma.csv`):
```bash
java -jar target/pdi-lab.jar --input images/input/Kurama.jpg --operation histograma
```
### Módulo 1.3: Convolução e Filtragem Espacial
* **Suavização / Filtro de Média:**
```bash
java -jar target/pdi-lab.jar --input images/input/Kurama.jpg --output images/output/suave.jpg --operation suavizacao --filtro ponderada3 --borda replicar
```
* **Filtro Laplaciano** (Gera automaticamente a versão bruta e a imagem realçada):
```bash
java -jar target/pdi-lab.jar --input images/input/Kurama.jpg --output images/output/laplaciano.jpg --operation laplaciano --borda replicar

```
* **Detector de Bordas Sobel** (Gera automaticamente 4 arquivos: $G_x$, $G_y$, Magnitude Aproximada e Magnitude Euclidiana):
```bash
java -jar target/pdi-lab.jar --input images/input/Kurama.jpg --output images/output/sobel.jpg --operation sobel --borda replicar
```