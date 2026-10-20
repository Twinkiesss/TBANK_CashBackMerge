# Cashback Merge

Мини-игра в духе 2048 с кэшбэком на поле из конфига.

## Запуск

    ./gradlew run
    ./gradlew check

## Симуляция

    ./gradlew run --args="simulate --config config/campaign.example.json --players 1000 --seed 42 --output report.md"

## Что реализовано в MR1

- Модель поля и плиток (`model.Board`, `model.Tile`).
- Конфиг кампании (`config.CampaignConfig`) с загрузкой из JSON и валидацией.
- Начальное состояние по seed (`model.GameState.createInitial`).
- Текстовый вывод поля (`cli.BoardPrinter`).

## Что реализовано в MR2

- Движок ходов (`model.GameState.applyMove`).
- Сдвиг и слияние по правилам 2048 (без двойного слияния за ход).
- Невалидный ход не тратит лимит и не спавнит плитку.
- Подсчёт score при слияниях.

## Что реализовано в MR3

- Таблица наград (`simulation.RewardTable`).
- Стратегия случайного выбора (`simulation.RandomStrategy`).
- Batch-симуляция (`simulation.Simulator`).
- Генератор Markdown-отчёта (`simulation.ReportWriter`).
- CLI-команда `simulate` (на Picocli).
- Проверка бизнес-ограничения: `actualMaxRewardRate <= maxRewardAllowedRate`.

## Пример отчёта

    # Отчёт симуляции Cashback Merge

    ## Параметры

    - **Игроков:** 1000
    - **Seed:** 42
    - **Стратегия:** random
    - **Лимит ходов:** 100

    ## Результаты

    - **Средний score:** 1450.25
    - **Максимальный score:** 4820
    - **Доля игроков с макс. наградой:** 15.50%

    ## Распределение наград

    - 50: 800
    - 150: 150
    - 500: 50

    ## Статус

    **OK**

## Решения

- Вероятности и проценты хранятся в `BigDecimal` — сумма `0.85 + 0.15` точно равна `1`.
- Размер поля берётся из конфига (`boardSize`), а не захардкожен.
- Валидация конфига: `movesLimit > 0`, `boardSize > 0`, `spawnRules` не пуст,
  вероятности положительны и в сумме дают ровно 1.
- Игра завершается не только по лимиту ходов, но и если на поле больше нет доступных слияний (`Board.canMove`).
## Бонусные задания

Помимо основной игры, в проекте реализованы следующие бонусы:

- **Консольная игра (3 балла):** Запуск через `./gradlew run --args="play"`. Управление: `w/a/s/d`, выход `q`.
- **Стратегия Greedy (5 баллов):** Доступна в симуляции через `--strategy greedy`. Максимизирует слияния.
- **Генеративные тесты (5 баллов):** Класс `PropertyBasedTest` проверяет сохранение суммы плиток на случайных полях.
- **Воспроизведение партии (3 балла):**
    - Запись: `./gradlew run --args="replay --seed 42 --output replay.json"`
    - Воспроизведение: `./gradlew run --args="replay --play --output replay.json"`
- **Сравнение кампаний (5 баллов):** `./gradlew run --args="compare --config1 config/campaign.example.json --config2 config/campaign.example.json"`
