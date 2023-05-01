Los requerimientos son los siguientes:

1. Debes desarrollar una API REST en Spring Boot utilizando java 11 o superior, con las siguientes funcionalidades:

  - Debe contener un servicio llamado por api-rest que reciba 2 números, los sume, y le aplique una suba de un porcentaje que debe ser adquirido de un servicio externo (por ejemplo, si el servicio recibe 5 y 5 como valores, y el porcentaje devuelto por el servicio externo es 10, entonces (5 + 5) + 10% = 11). Se deben tener en cuenta las siguientes consideraciones:
    - El servicio externo puede ser un mock, tiene que devolver el % sumado.
    - Dado que ese % varía poco, se debe de garantizar que esa información expire cada 30 min.
    - Si el servicio externo falla, se debe devolver el último valor retornado. Si no hay valor, debe retornar un error la api.
    - Si el servicio falla, se puede reintentar hasta 3 veces.

  - Historial de todos los llamados a todos los endpoint junto con la respuesta en caso de haber sido exitoso. Responder en Json, con data paginada. El guardado del historial de llamadas no debe sumar tiempo al servicio invocado, y en caso de falla, no debe impactar el llamado al servicio principal.
  - La api soporta recibir como máximo 3 rpm (request / minuto), en caso de superar ese umbral, debe retornar un error con el código http y mensaje adecuado.
  - El historial y la información de los usuarios se debe almacenar en una database PostgreSQL.
  - Incluir errores http. Mensajes y descripciones para la serie 4XX.

2.    Se deben incluir tests unitarios.
3.    Esta API debe ser desplegada en un docker container. Este docker puede estar en un dockerhub público. La base de datos también debe correr en un contenedor docker. Recomendación usar docker compose.
4.    Debes agregar un Postman Collection o Swagger para que probemos tu API.
5.    El código debe ser enviado adjunto por correo, no en un repositorio público, junto con las indicaciones de prueba.
6.    Tener en cuenta que la aplicación funcionará de la forma de un sistema distribuido donde puede existir más de una réplica del servicio funcionando en paralelo.
