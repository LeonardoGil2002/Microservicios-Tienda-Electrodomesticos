# Microservicios-Tienda-Electrodomesticos

Aplicación basada en la arquitectura de software de microservicios. Se utilizan los patrones de diseño de Service Registry, Service Recovery, Load Balancing, Circuit Breaker, API Gateway y Server Config.

Está formada por 6 microservicios:
- Products-service: se encarga de gestionar la información de los electrodomésticos disponibles.
- Carts-service: contiene un CRUD. Este servicio consume de Products-service para poder listar los carritos con sus productos y la información de estos. También puede añadir y borrar productos individuales de un carrito específico.
- Sales-service: también contiene un CRUD. Registra la venta con un id y cada venta está asociada a un único carrito. Consume de Carts-service.
- Eureka Server: se utiliza para registrar cada uno de los microservicios, para cumplir así con el patrón de diseño de Service Registry y Service Recovery.
- API Gateway: se utiliza para actuar como punto de entrada para los clientes externos.
- Server Config: proporciona una visión unificada de la configuración y una forma segura y eficiente de gestionar las actualizaciones en nuestros microservicios que compartan las mismas entre si. Cada servicio puede consumir o consultar sus configuraciones desde este servidor.

Tecnologías utilizadas:
- Java 17
- Spring Boot
- JPA
- MySQL
- Postman
