# Задача backend vs frontend
***
Бэкенд-разработчики сказали, что они всё уже сделали, это фронтендщики тормозят. Поэтому функцию перевода денег с карты на карту мы протестировать через веб-интерфейс не можем.

Зато они выдали нам описание REST API, которое позволяет это сделать, использовать нужно app-deadline.jar.

Вот описание API:

- Логин

POST http://localhost:9999/api/auth  
Content-Type: application/json


{  
  "login": "vasya",  
  "password": "qwerty123"  
}

- Верификация

POST http://localhost:9999/api/auth/verification  
Content-Type: application/json

{  
  "login": "vasya",  
  "code": "599640"  
}  

В ответе, в поле «token» придёт токен аутентификации, который нужно использовать в последующих запросах.

- Просмотр карт

GET http://localhost:9999/api/cards  
Content-Type: application/json  
Authorization: Bearer {{token}}  

Где {{token}} — это значение «token» с предыдущего шага.

- Перевод с карты на карту (любую)
POST http://localhost:9999/api/transfer  
Content-Type: application/json  
Authorization: Bearer {{token}}  

{  
  "from": "5559 0000 0000 0002",  
  "to": "5559 0000 0000 0008",  
  "amount": 5000  
}

#### Задание:
##### Внимательно изучите запросы и ответы и, используя любой инструмент, который вам нравится, реализуйте тесты API.
