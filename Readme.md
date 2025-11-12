# User Service
## Ejecución local

```bash
mvn clean install
mvn spring-boot:run

El servicio se ejecuta en: http://localhost:8081/

| Método | Endpoint                                  | Descripción                  |
| ------ | ----------------------------------------- | ---------------------------- |
| POST   | `/bs-usuarios/gestion-usuarios/v1/crear`  | Crear un nuevo usuario       |
| GET    | `/bs-usuarios/gestion-usuarios/v1/listar` | Listar todos los usuarios    |
| GET    | `/bs-usuarios/gestion-usuarios/v1/{id}`   | Obtener un usuario por su ID |
