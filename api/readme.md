# Sobre el proyecto

Se consumió una api externa y se realizo el registro de las peticiones en Postgresql


# Tecnologías
- NodeJS
- ExpressJS
- Typeorm
- Postgresql
- Typescript

# Instalacion
- Clonar el proyecto
	- `git clone`
	- `cd api`
	
- Instalar dependencias 
	- `npm install`

- Crear la base de datos en postgresql  ***De preferencia con el nombre i4digital cambiar solamente si es necesario***
- Configurar Base de datos en **src/db.ts**  --- ***Solo si es necesario***
    - `const HOSTDB = "localhost"`
    - `const PORTDB = 5432`
	- `const USERDB = "postgres"`
	- `const PWDB = "admin"`
	- `const DB = "i4digital"`

- Configurar puerto en **src/environment.dev.ts**  --- ***solo si es necesario***
    - `public static API_ENDPOINT='http://127.0.0.1:4000/api/'`
    - `public static PORT = 4000`

## Pruebas
- Ejecutar test unitario
	- `npm test`

## Entorno de desarrollo
- Correr servidor de desarrollo
	- `npm run dev`

## Contruir proyecto
- Contruir proyecto
	- `npm run build`
- Iniciar servidor
	- `npm start`
