# Processamento de Imagens - Laboratórios M1 e M2

**Autor:** João Matheus de Oliveira Vieira  
**Curso:** Ciência da Computação  
**Disciplina:** Processamento de Imagens Digitais (PDI)  
**Ano/Semestre:** 2026-02  

---

## Sobre o Projeto

Este projeto consiste em uma aplicação unificada de linha de comando (CLI) desenvolvida em Java, contemplando todas as atividades práticas dos módulos M1 e M2 da disciplina de PDI. 

* **Módulo 1 (M1.1 a M1.3):** Implementação manual de algoritmos de percurso de pixels, transformações de intensidade (brilho, contraste, limiarização) e filtragem espacial/convolução (suavização, Laplaciano, Sobel), respeitando a restrição de não utilizar bibliotecas de alto nível.
* **Módulo 2 (M2.1 e M2.2):** Integração com OpenCV para segmentação avançada, rotulação de componentes conexos e análise morfológica (Abertura e Fechamento), extraindo propriedades métricas das imagens (CSVs e JSON).

---

## Pré-requisitos e Dependências

Para compilar e executar o projeto, você precisará apenas de:
- **Java Development Kit (JDK):** Versão 11 ou superior.
- **Apache Maven:** Para gerenciamento do build.

> **Nota sobre o OpenCV:** A biblioteca OpenCV (`org.openpnp:opencv`) está declarada no `pom.xml` e é empacotada automaticamente pelo Maven. Não é necessária nenhuma instalação prévia do OpenCV no sistema operacional, garantindo total portabilidade entre Windows, Linux e macOS.

---

## Como Compilar e Testar

Na raiz do projeto (onde está o `pom.xml`), limpe compilações anteriores, execute os testes unitários e empacote o executável com o comando:

```bash
mvn clean package
```

O arquivo final será gerado em `target/pdi-lab.jar`.

---

## Uso Geral (CLI)

A sintaxe base da aplicação é:

```bash
java -jar target/pdi-lab.jar --input <caminho> --operation <nome_operacao> [argumentos_opcionais]
```

### Argumentos da CLI:
- `--input <path>`: (Obrigatório) Caminho da imagem ou máscara de entrada.
- `--operation <nome>`: (Obrigatório) Operação a ser executada.
- `--output <path>`: (Opcional) Caminho de saída (ou prefixo, no caso da M2.2).
- `--value <num>`: (M1) Valor numérico para brilho, contraste ou limiar.
- `--filtro <tipo>`: (M1) Kernel de suavização (`media3`, `ponderada3`, `media5`).
- `--borda <tipo>`: (M1) Tratamento de borda na convolução (`copiar`, `replicar`).
- `--method <tipo>`: (M2) Método de segmentação OpenCV (`fixed`, `otsu`, `adaptive`). Padrão: `fixed`.
- `--threshold <num>`: (M2) Valor de corte para o método `fixed`. Padrão: `128.0`.
- `--connectivity <4|8>`: (M2) Conectividade para análise de componentes. Padrão: `8`.
- `--min-area <num>`: (M2) Área mínima (em pixels) para filtrar componentes. Padrão: `0`.

---

## Exemplos de Execução - Módulo 2 (OpenCV)

### M2.1: Segmentação e Geração de Máscara Binária
Gera uma máscara no formato obrigatório `.png` (sem perdas), convertendo a imagem em fundo (0) e objeto (255).

* **Método Fixo (Limiar manual):**
  ```bash
  java -jar target/pdi-lab.jar --input images/input/Kurama.jpg --output images/output/mask_fixed.png --operation segment --method fixed --threshold 120
  ```
* **Método Otsu (Limiar automático):**
  ```bash
  java -jar target/pdi-lab.jar --input images/input/Kurama.jpg --output images/output/mask_otsu.png --operation segment --method otsu
  ```
* **Método Adaptativo (Iluminação irregular):**
  ```bash
  java -jar target/pdi-lab.jar --input images/input/Kurama.jpg --output images/output/mask_adapt.png --operation segment --method adaptive
  ```

### M2.2: Análise de Componentes e Morfologia
Lê uma máscara binária, aplica uma sequência morfológica (Abertura seguida de Fechamento) e extrai estatísticas dos componentes conexos.

```bash
java -jar target/pdi-lab.jar --input images/output/mask_fixed.png --output results/kurama --operation analyze --connectivity 8 --min-area 50
```
> O argumento `--output` na operação `analyze` funciona como um prefixo. O comando acima gerará automaticamente os seguintes arquivos na pasta `results/`:
> * `kurama_initial.csv` (Métricas antes da morfologia)
> * `kurama_final.csv` (Métricas após morfologia e filtro de área)
> * `kurama_morph1_open.png` (Resultado do 1º estágio: Abertura)
> * `kurama_morph2_close.png` (Resultado do 2º estágio: Fechamento)
> * `kurama_labels.png` (Imagem de visualização com componentes coloridos)
> * `kurama_summary.json` (Resumo geral das contagens e parâmetros)

---

## Exemplos de Execução - Módulo 1 (Implementação Manual)

* **Inspecionar metadados:**
  ```bash
  java -jar target/pdi-lab.jar --input images/input/img.jpg --operation inspect
  ```
* **Cópia exata:**
  ```bash
  java -jar target/pdi-lab.jar --input images/input/img.jpg --output images/output/copia.jpg --operation copy
  ```
* **Brilho:**
  ```bash
  java -jar target/pdi-lab.jar --input images/input/img.jpg --operation brilho --value 50
  ```
* **Contraste:**
  ```bash
  java -jar target/pdi-lab.jar --input images/input/img.jpg --operation contraste --value 1.5
  ```
* **Negativo:**
  ```bash
  java -jar target/pdi-lab.jar --input images/input/img.jpg --operation negativo
  ```
* **Limiarização manual:**
  ```bash
  java -jar target/pdi-lab.jar --input images/input/img.jpg --operation limiarizacao --value 128
  ```
* **Histograma CSV:**
  ```bash
  java -jar target/pdi-lab.jar --input images/input/img.jpg --operation histograma
  ```
* **Suavização (Convolução):**
  ```bash
  java -jar target/pdi-lab.jar --input images/input/img.jpg --operation suavizacao --filtro ponderada3 --borda replicar
  ```
* **Laplaciano e Sobel:**
  ```bash
  java -jar target/pdi-lab.jar --input images/input/img.jpg --operation laplaciano
  ```
  ```bash
  java -jar target/pdi-lab.jar --input images/input/img.jpg --operation sobel
  ```
