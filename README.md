# АвтоМаг

## Описание проекта
Данный проект представляет собой сервис по продаже машин.
На сайте располагаются объявления. 

Объявлении состоит:
- описание
- марка машины
- тип кузова
- фото
- статус о продаже

## Стек технологий
* Spring Boot
* Thymeleaf
* Bootstrap
* Liquibase
* Hibernate
* Lombok
* H2
* PostgreSQL

## Окружение
Java 17, Maven 3.8, PostgreSQL 14, Hibernat-Core 5.6.11, H2 2.1.214.

## Запуск проекта
- Импортировать проект в IntelliJ IDEA
- В PostgreSQL создать БД cars
- В Maven выполнить команду Plugins\liquibase\liquibase:update
- Выполнить метод main
- Открыть веб-браузер по адресу: http://127.0.0.1:8080/user/authorization