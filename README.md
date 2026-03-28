 # 🕒 PontoFácil - Sistema de Registro de Ponto Android
​
 O **PontoFácil** é um aplicativo Android moderno desenvolvido em Kotlin, projetado para facilitar e tornar mais seguro o registro de jornada de trabalho. O app captura não apenas o horário, mas também a **foto do colaborador** e sua **localização exata via GPS**, garantindo a integridade dos dados.
​

​
 ## 🚀 Funcionalidades
​
 - **Autenticação Segura:** Tela de login com validação e persistência de sessão.
 - **Registro de Ponto Inteligente:**
   - Captura de foto em tempo real usando **CameraX**.
   - Localização geográfica automática via **Google Play Services Location**.
   - Seleção de tipo de registro (Entrada, Intervalo ou Saída).
 - **Histórico Offline:** Visualização de todos os registros salvos localmente no dispositivo.
 - **Banco de Dados Local:** Armazenamento robusto utilizando **Room Database**.
 - **Interface Moderna:** UI baseada em **Material Design 3** e **ViewBinding**.
​
 ---
​
 ## 🛠️ Tecnologias Utilizadas
​
 - **Linguagem:** [Kotlin](https://kotlinlang.org/)
 - **Arquitetura:** MVVM (Model-View-ViewModel)
 - **Câmera:** [CameraX](https://developer.android.com/training/camerax)
 - **Localização:** [Fused Location Provider API](https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderClient)
 - **Banco de Dados:** [Room](https://developer.android.com/training/data-storage/room)
 - **Injeção de Dependência:** ViewModel Factory
 - **Corrotinas:** Para operações assíncronas e fluidez da UI.
 - **Compatibilidade:** Suporte atualizado para Android 15 (alinhamento de 16KB).
​
 ---
​
 ## 📦 Como Instalar e Rodar
​
 ### Pré-requisitos
 - Android Studio Jellyfish ou superior.
 - Java JDK 17 ou 21.
 - Dispositivo Android físico ou Emulador (API 24+).
​
 ### Passo a Passo
 1. Clone este repositório:
    ```bash
    git clone https://github.com/seu-usuario/ponto-facil.git
    ```
 2. Abra o projeto no **Android Studio**.
 3. Sincronize o Gradle clicando no ícone do elefante.
 4. Conecte seu celular com **Depuração USB** ativa.
 5. Clique em **Run** (Triângulo Verde).
​
 ---
​
 ## 📈 Próximas Melhorias
​
 - [ ] Implementação de troca de câmera (Frontal/Traseira).
 - [ ] Sincronização automática com API externa/Nuvem.
 - [ ] Notificações push para lembrete de horários.
 - [ ] Exportação de relatórios em PDF/Excel.
​
 ---

 *Desenvolvido com ❤️ por Vitor marden *
