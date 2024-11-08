# RetoSignInSignUp-Server
URLs, repositorios. https://github.com/osrc-a/RetoSignUpSignIn_Client.git https://github.com/lucia9928/RetoSignInSignUp-Server.git https://github.com/lucia9928/Reto-sign-in-Sign-up-libreria.git

markel0707--> Markel Arabiourrutia lucia9928 --> Melany Salinas osrc-a --> Oscar Gonzalez AndoniOrd --> Andoni Ordoñez

Importante:

1.Archivo de propiedades, configuararlo acorde lo que tu base de datos disponga, usuarios, contraseñas, url etc.

El parametro URL, USER, PASSWORD. Asegurar de que esta acorde a tus necesidades.

2.Asegurarse de tener campos de base de datos como notification_type que no viene por defecto si no tienes proyecto y parte de horas activados en la base de datos de odoo.

Ejecutar el jar. Cuando creas un proyecto de aplicación Java que tiene una clase principal, el IDE copia automáticamente todos los archivos JAR en el classpath del proyecto a la carpeta dist/lib del proyecto. El IDE también agrega cada uno de los archivos JAR al elemento Class-Path en el archivo de manifiesto JAR de la aplicación (MANIFEST.MF).

Importante, hacer la ejecución en 2 instancias de consola diferentes.(Es decir, abrir una consola, ejecutar el servidor, abrir otra y ejecutar el cliente.)

Para ejecutar el proyecto desde la línea de comandos, ve a la carpeta dist de cada proyecto y escribe lo siguiente: ej. cd C:/.../.../.../dist java -jar "RetoSignInSignUpServer.jar" A la hora de cerrar el servidor, hay que darle Ctrl + c, o cerrar la consola. ej. cd C:/.../.../.../dist java -jar "RetoSignInSignUpClient.jar"

Descripción del Proyecto:

Este proyecto ha sido desarrollado en Java utilizando la API de JavaFX, con el objetivo de proporcionar una interfaz gráfica de usuario (GUI) para gestionar el registro e inicio de sesión de usuarios. El sistema se conecta a una base de datos para realizar la inserción de nuevos usuarios y la consulta de credenciales para el inicio de sesión.

Tanto el registro como el inicio de sesión incluyen una serie de validaciones que deben cumplirse, como la verificación de campos obligatorios, la validación de formatos y la comprobación de la coincidencia de datos introducidos, como contraseñas.

El proyecto soporta múltiples conexiones simultáneas de clientes, permitiendo que varios usuarios interactúen con el sistema de manera concurrente, sin interferir entre ellos. Esto se logra mediante la gestión eficiente de los recursos compartidos y el manejo adecuado de las conexiones.

El sistema está compuesto por tres programas principales:

Servidor: Encargado de gestionar las conexiones de los clientes, procesar las solicitudes y mantener la integridad de los datos en la base de datos. Cliente: Interfaz que permite a los usuarios interactuar con el sistema, ya sea para registrarse o iniciar sesión. Librería compartida: Un módulo común que contiene las funciones y clases utilizadas tanto por el servidor como por el cliente, facilitando la reutilización del código y mejorando la estructura del proyecto.

