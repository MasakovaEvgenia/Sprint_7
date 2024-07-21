# Тестирование API учебного сервиса Яндекс.Самокат

## Описание
Проект предназначен для тестирования API учебного сервиса Яндекс.Самокат. В тесте проверяется создание курьера, логин курьера, создание заказа и список заказов.

## Ссылки
- [Сервис Яндекс.Самокат](https://qa-scooter.praktikum-services.ru/)
- [Документация API](https://qa-scooter.praktikum-services.ru/docs/#api-entity.Courier-Login)

## Технологии

| Технология                | Версия  |
|---------------------------|---------|
| Java                      | 11      |
| JUnit                     | 4.13.2  |
| Maven                     | 3.8.1   |
| allure.version            | 2.15.0  |
| groovy                    | 3.0.8   |
| gson                      | 2.8.9   |
| maven-surefire-plugin     | 2.22.2  |
| allure-maven              | 2.10.0  |

## Создание отчёта Allure

```sh
# добавляем папку с отчётом Allure к файлам. Ключ -f пригодится, если папка target указана в .gitignore
git add -f .\target\allure-results\.

# выполняем коммит
git commit -m "add allure report"

# отправляем файлы в удалённый репозиторий
git push
