# CHENTS - Sistema de Alerta de Enchentes

![Logo do projeto](https://github.com/user-attachments/assets/3c0bf63a-5bbf-4c68-b222-d2606ec31377)

## Sobre o Projeto

O CHENTS (Sistema de Alerta de Enchentes) é uma aplicação web desenvolvida para mitigar os impactos de eventos climáticos extremos, especificamente enchentes, fornecendo alertas em tempo real e informações cruciais para a população. O sistema utiliza crowdsourcing notificar usuários em áreas de risco.

### Tecnologias Utilizadas

- **Backend**: Spring MVC, Spring Security, Spring AI
- **Frontend**: Thymeleaf, HTML, CSS
- **Mensageria**: RabbitMQ
- **Autenticação**: OAuth 2.0, Spring Security
- **Consumer** com Spring AI, Mongodb e Telegram API
- **Testes**: JUnit, Mockito, Spring Test

## Funcionalidades Principais

- **Monitoramento em Tempo**: Acompanhamento de alertas de enchentes em diferentes regiões em tempo real
- **Sistema de Alertas**: Notificações automáticas para usuários em áreas de risco através da API do Telegram através da aplicação consumidora em [chents=consumer](github.com/eduardofuncao/chents-consumer)

### Passos para Execução

1. Clone o repositório:
   ```bash
   git clone https://github.com/eduardofuncao/chents.git
   ```

2. Configure o arquivo de propriedades:
   ```
   src/main/resources/application.properties
   ```

3. Execute o aplicativo:
   ```bash
   mvn spring-boot:run
   ```

4. Acesse o sistema em:
   ```
   http://localhost:8080
   ```

*Como alternativa, o projeto completo pode ser executado através do arquivo docker-compose.yml utilizando:*
```bash
docker-compose up -d
```

## Testes

ALTERAR COM INSTRUÇÕES REAIS
Para executar os testes automatizados:

```bash
mvn test
```

## Diagrama Entidade Relacionamento

FALTA DIAGRAMA
FALTA DIAGRAMA
FALTA DIAGRAMA

## Arquitetura do Sistema

FALTA DIAGRAMA
FALTA DIAGRAMA
FALTA DIAGRAMA

## Equipe
    Eduardo Função - RM553362
    Artur Fiorindo - RM553481
    Jhoe Hashimoto - RM553831


## Links Importantes

- [Deploy da Aplicação](#)
- [Vídeo de Demonstração](#)
- [Vídeo Pitch](#)

---

*Desenvolvido como parte do projeto final da Global Solution para a disciplina Java Advanced*
