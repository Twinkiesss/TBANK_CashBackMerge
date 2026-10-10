# Cashback Merge

Мини-игра в духе 2048 с кэшбэком на поле из конфига.

## Запуск

    ./gradlew run
    ./gradlew check

## Что реализовано в MR1

- Модель поля и плиток (`model.Board`, `model.Tile`).
- Конфиг кампании (`config.CampaignConfig`) с загрузкой из JSON и валидацией.
- Начальное состояние по seed (`model.GameState.createInitial`).
- Текстовый вывод поля (`cli.BoardPrinter`).

## Решения

- Вероятности и проценты хранятся в `BigDecimal` — сумма `0.85 + 0.15` точно равна `1`.
- Размер поля берётся из конфига (`boardSize`), а не захардкожен.
- Валидация конфига: `movesLimit > 0`, `boardSize > 0`, `spawnRules` не пуст,
  вероятности положительны и в сумме дают ровно 1.
