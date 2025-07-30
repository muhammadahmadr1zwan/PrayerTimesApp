# Prayer App Backend

A Spring Boot backend API for prayer times.

## API Endpoints

- `GET /api/prayer-times/{date}` - Get prayer times for a specific date

## Example Response

```json
{
  "date": "2025-07-30",
  "prayers": [
    {
      "name": "Fajr",
      "athan": "04:45:00",
      "iqamah": "05:05:00"
    }
  ]
}
```

## Local Development

```bash
mvn spring-boot:run
```

## Deployment

This app is configured for Railway deployment. 