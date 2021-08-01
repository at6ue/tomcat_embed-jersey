# Playground for Jersey on Embedded Tomcat

## How to start

`gradlew run` to launch the server on port 8080.

## Contents

- [(root)](http://localhost:8080/) is the context root and will return a static content.
- [basic](http://localhost:8080/basic) is routed in `web.xml` and will return a plaintext message.
- [app](http://localhost:8080/app) is the the root resource of the `app` application path, and is the same resource as `basic`.
- [app/greeting](http://localhost:8080/app/greeting) is a demonstration of dependency injection.
- [app/positive](http://localhost:8080/app/positive) is a demonstration of transaction.
  - GET shows the current value.
  - PUT a positive or negative number into the request body to add the value, but the value will never be negative.
- [app/validation/{id}](http://localhost:8080/app/validation/1234CDEF?name=John&age=25&height=170&weight=65) is a demonstration of Bean Validation.

## Author
atti

## License
This project is licensed under the MIT License - see the LICENSE.md file for details