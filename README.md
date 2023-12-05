[![Finalizado](https://img.shields.io/badge/Status-Conclu%C3%ADdo-brightgreen)](https://github.com/imetropoledigital/trabalho-final-matheus-costa-vidal)

<p>
<img src="docs/imgs/HomeManager.png" alt="Logo Home Manager" height="100">
</p>

O Home Manager é um aplicativo intuitivo e prático desenvolvido para simplificar a gestão de tarefas diárias e semanais em uma casa. Com o HomeManager, os membros de uma residência podem criar perfis individuais, cadastrar outros membros da casa e colaborar para manter a organização e eficiência nas atividades do lar. ¡Sua casa, sua organização!

## Índice
- 🔨 [Funcionalidades](#-funcionalidades)
- 📁 [Estrutura do projeto](#-estrutura-de-diretórios)
- 📊 [Diagrama de classes](#-diagrama-de-classes)
- 💻 [Técnicas e tecnologias utilizadas](#-técnicas-e-tecnologias-utilizadas)
  - 🗃️ [Classes e Componentes JavaFX Utilizados](#%EF%B8%8F-classes-e-componentes-javafx-utilizados)
- 🔧 [Como executar](#-como-executar)
- 👥 [Autores](#-autores)
 
# 🔨 Funcionalidades

<!--
<p align="center">
  <img src="docs/imgs/menu.png" alt="Menu principal">
  <img src="docs/imgs/preenchertask.png" alt="Preencher tarefa">
</p>

<p align="center">
      <img src="docs/imgs/menueditar.png" alt="Menu: editar tarefa">
      <img src="docs/imgs/menuordenar.png" alt="Menu: ordenar o quadro">
      <img src="docs/imgs/menusalvar.png" alt="Menu: salvar o quadri">
</p>-->

O projeto Home Manager, um aplicativo dedicado ao gerenciamento eficiente de tarefas domésticas, oferece uma série de funcionalidades para promover a organização e colaboração dentro de uma residência:

- **Cadastro de casa: 🏠** É possível criar um usuário de toda a casa, cadastrando os membros que residem;

- **Cadastro de membros: 👥** Os usuários podem criar perfis individuais, inserindo os nomes;

- **Gestão de tarefas diárias e semanais: 📅** Os membros podem cadastrar tarefas diárias e semanais, atribuindo responsabilidades específicas a cada membro da casa;

- **Distribuição justa de tarefas: ⚖️** Funcionalidade de distribuição equitativa de tarefas, garantindo que cada membro contribua de maneira justa para o funcionamento da casa. Tarefas são distribuídas aleatoriamente, levando em consideração o número de membros e a quantidade de tarefas disponíveis;

- **Acompanhamento do progresso: 📈** Os membros podem marcar as tarefas como concluídas, proporcionando uma visão clara do progresso das atividades domésticas.

- **Estatísticas visuais 📊** Gráficos e estatísticas visuais auxiliam no monitoramento do desempenho individual e coletivo;

- **Edição de lista de tarefas semanais e diárias: 📝** 
  - **Adicionar tarefas personalizadas:** Os usuários podem adicionar tarefas personalizadas à lista da casa;
  - **Remover tarefas:** Permite aos usuários remover tarefas específicas que não são mais relevantes ou necessárias;

- **Edição da lista de membros da casa: 👥** 
  - **Adicionar novos membros:** Os usuários podem adicionar novos membros à lista, inserindo seus nomes para criar perfis individuais;
  - **Remover membros** Permite remover membros que não residem mais na casa ou que não desejam mais fazer parte do grupo;

- **Reiniciar a Semana: 🔄**
  - **Resetar o progresso:** Opção para reiniciar o progresso de todas as tarefas da casa, marcando todas como "não realizadas";
  - **Limpar tarefas dos membros:** Remove todas as tarefas atribuídas aos membros, começando uma nova semana do zero;


- **Visualização de perfil individual: 👤** Oferece uma visualização personalizada para cada membro, permitindo que eles visualizem as tarefas atribuídas a si mesmos, marquem-nas como concluídas e acompanhem seu progresso por meio de uma barra de progresso.

- **Visualização geral: 👀** O aplicativo oferece uma visualização geral das tarefas, permitindo que os usuários vejam rapidamente todas as tarefas da casa;

- **Armazenamento em arquivo binário: 📁** Os dados da casa são armazenados em um arquivo binário, permitindo a recuperação dos dados para uso posterior;

- **Carregamento de arquivo binário: ⬆️** Todos os usuários são carregados partir de um arquivo binário, recuperando o estado anterior da casa;

- **Privacidade e Segurança: 🔐** Prioriza a segurança dos dados, garantindo que informações pessoais e atividades domésticas estejam protegidas.

O Home Manager é uma solução abrangente para famílias e casas compartilhadas, oferecendo uma abordagem colaborativa para o gerenciamento de responsabilidades diárias. Transforme a gestão doméstica em uma experiência organizada e eficiente com o Home Manager.

# 📁 Estrutura de diretórios
- **/docs:** Contém a documentação do projeto;
- **/imgs:** Imagens utilizada para a documentação;
- **/src:** Contém o código fonte do projeto;
  - **/application:** Contém a classe principal da aplicação;
  - **/controller:** Responsável por controladores da aplicação;
  - **/event:** Gerencia eventos para comunicação entre controladores;
  - **/model:** Contém os modelos (models) do projeto;
  - **/repository:** Repositório envolvendo operações de armazenamento e recuperação de dados;
- **/storage** Arquivo de dados salvo em binário (.dat) para recuperação;
- **.gitignore:** Arquivo do Git para ignorar arquivos no controle de versão;
- **README.md:** Documentação essencial do projeto em texto.

# 📊 Diagrama de classes
O diagrama de classes UML é uma representação visual da estrutura e das relações entre as classes em um projeto. Ele fornece uma visão geral da organização das classes, seus atributos e métodos, bem como as associações, heranças e dependências entre elas. Este diagrama é uma ferramenta poderosa para entender a arquitetura do sistema, identificar as principais entidades e suas interações, e facilitar o desenvolvimento, a manutenção e a comunicação entre os membros da equipe. Se você deseja explorar mais detalhes do diagrama de classes [clique aqui](docs/diagrama/) para ser redirecionado ao arquivo PDF correspondente.

# 💻 Técnicas e tecnologias utilizadas
<!--
<div style="display: flex; flex-wrap: wrap; justify-content: center; align-items: center;">
  <img src="docs/imgs/cpplogo.png" alt="Logo C++" height="70" style="margin-right: 20px;">
  <img src="docs/imgs/vscode.png" alt="Logo VSCode" height="70" style="margin-right: 20px;">
  <img src="docs/imgs/estrutura.png" alt="Logo Estrutura" height="70" style="margin-right: 20px;">
  <img src="docs/imgs/gcc.png" alt="Logo GCC" height="70" style="margin-right: 20px;">
  <img src="docs/imgs/github.png" alt="Logo GitHub" height="70" style="margin-right: 20px;">
  <img src="docs/imgs/git.png" alt="Logo Git" height="70" style="margin-right: 20px;">
  <img src="docs/imgs/uml.png" alt="Logo UML" height="70" style="margin-right: 20px;">
  <img src="docs/imgs/doxygen.png" alt="Logo Doxygen" height="50" style="margin-right: 20px;">
</div>-->

- **Java:** Linguagem de programação de alto nível, amplamente usada para desenvolvimento de aplicativos de software;
- **JavaFX:** Plataforma para criar aplicativos de interface gráfica de usuário (GUI) em Java;
- **FXML:** Linguagem de marcação utilizada no JavaFX para criar interfaces de usuário de forma declarativa;
- **CSS:** Linguagem de estilo usada para estilizar a aparência das interfaces gráficas JavaFX;
- **Javadoc:** Ferramenta para gerar documentação a partir de código-fonte Java, fornecendo referências e documentação dos métodos;
- **Maven:** Ferramenta de automação de compilação e gerenciamento de projetos em Java;
- **Generics:** Recurso do Java que permite a criação de classes, interfaces e métodos genéricos que aceitam tipos como parâmetros;
- **Polimorfismo:** Capacidade de objetos de diferentes classes serem tratados por um mesmo tipo genérico, permitindo que métodos se comportem de maneiras diferentes em diferentes classes;
- **Git & Github:** Sistema de controle de versão distribuído (Git) e plataforma de hospedagem de código (Github);
- **Diagrama UML:** Conjunto de notações e diagramas para modelar sistemas de software;
- **Modularização:** Técnica de dividir um sistema em módulos independentes para melhorar a manutenção e a escalabilidade;
- **Event Handling:** Tratamento de eventos gerados por interações do usuário (por exemplo, cliques de botões, teclas pressionadas, etc.);
- **Design Patterns:** Soluções recorrentes para problemas comuns de design de software, fornecendo abordagens testadas e comprovadas;
- **Princípios SOLID:** Conjunto de princípios de design de software (Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation e Dependency Inversion);
- **Estrutura de dados:** Métodos, estruturas e algoritmos para armazenar e organizar dados de forma eficiente;
- **Segurança de dados:** Práticas e técnicas para proteger informações sensíveis contra acesso não autorizado ou alterações indevidas, incluindo criptografia hash para senhas;
- **Persistência de dados:** Mecanismos e técnicas para salvar e recuperar dados de forma permanente.
- **Separation of Concerns:** Princípio de design para separar diferentes preocupações em módulos independentes;
- **Manipulação de arquivos:** Operações de leitura, gravação e manipulação de arquivos em um sistema de arquivos;
- **Programação Orientada a Objetos:** Paradigma de programação baseado em objetos, incluindo conceitos como classes, objetos, herança, polimorfismo, etc;
- **Arquitetura Model-View-Controller:** Padrão de design que separa os componentes de uma aplicação em modelo (dados), visão (interface gráfica) e controlador (lógica de controle).

## 🗃️ Classes e Componentes JavaFX Utilizados
- FXML: Utilizado para definir a interface do usuário de forma declarativa.
- FXMLLoader: Utilizado para carregar arquivos FXML.
- Controller: Controlador responsável por gerenciar a lógica da interface do usuário.
- ScrollPane: Utilizado para adicionar uma barra de rolagem em torno de componentes maiores.
- ComboBox: Componente que oferece uma lista suspensa de opções para escolha.
- TextField: Caixa de texto que permite a entrada de dados do usuário.
- PasswordField: Campo de texto para entrada de senhas, ocultando os caracteres digitados.
- CheckBox: Componente que permite ao usuário selecionar ou desmarcar uma opção.
- Button: Componente para botões na interface gráfica.
- VBox: Container de layout vertical na interface gráfica.
- HBox: Contêiner de layout horizontal para organizar elementos lado a lado.
- Label: Componente para exibir texto na interface gráfica.
- ProgressBar: Utilizado para exibir o progresso em barras.
- Scene: Define o conteúdo do palco (Stage) em JavaFX.
- Stage: Janela principal do aplicativo JavaFX.


# 🔧 Como executar?
O aplicativo utiliza o Maven para facilitar o processo de compilação e execução. Siga as etapas abaixo para compilar e executar o projeto:

## Pré-requisitos
Certifique-se de ter o Maven instalado em seu sistema antes de prosseguir.

### Passo 1: Obtenha o código-fonte
Clone o repositório do projeto em seu ambiente local ou faça o download dos arquivos fonte em um diretório de sua escolha.

### Passo 2: Navegue para o diretório do aplicativo
Abra um terminal e navegue até o diretório raiz do projeto usando o comando cd:<br>
```
cd caminho/para/o/diretorio/do/projeto/HomeManager
```

### Passo 3: Compile e Execute o aplicativo
Execute o seguinte comando para compilar o aplicativo: <br>
```
mvn clean javafx:run
```
<br>
Isso iniciará o processo de compilação, baixando as dependências do Maven e executando o aplicativo JavaFX.

## Observações
- Certifique-se de que o arquivo pom.xml está presente no diretório raiz do projeto antes de executar o comando Maven.
- Ao finalizar a compilação, o aplicativo JavaFX será iniciado automaticamente.

## Em caso de dúvidas
Se você encontrar qualquer problema ou precisar de mais informações sobre como compilar o projeto, consulte a documentação do Maven ou entre em contato com a equipe de desenvolvimento.

- O uso do Maven simplifica o processo de compilação e gestão de dependências, proporcionando uma experiência mais eficiente no desenvolvimento do projeto Home Manager.

# 👥 Autores

| [<img src="https://avatars.githubusercontent.com/u/129897959?v=4" width=115><br><sub>Isabela Gomes</sub>](https://github.com/cyberisa) |  [<img src="https://avatars.githubusercontent.com/u/102569695?s=400&u=f20bbb53cc46ec2bae01f8d60a28492bfdccbdd5&v=4" width=115><br><sub>Matheus Vidal</sub>](https://github.com/matheusvidal21) |
| :---: | :---: |
