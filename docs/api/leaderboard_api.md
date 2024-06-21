# Leaderboard API Documentation

For keeping track of the scores on a global level, the professor of this subject created a [Leaaderboard API](https://github.com/Appollo41-Community/rma-quiz-leaderboard-api).

## Endpoints used

| Method | Endpoint |
|--------|-----------------------------------|
| GET    | https://rma.finlab.rs/leaderboard |
| POST   | https://rma.finlab.rs/leaderboard |

## Fetching the leaderboard
To fetch the global leaderboard, use the following request: <br>
**GET** `https://rma.finlab.rs/leaderboard`

**URL Parameters:** <br>
- `category` (required): The ID of the quiz category (1, 2 or 3)

Example request: <br>
**GET** `https://rma.finlab.rs/leaderboard?category=3`

## Posting the quiz result 
To post the quiz results to the leaderboardm use the following request:
**POST** `https://rma.finlab.rs/leaderboard`

**Request body:** <br>
```
{ 
    "nickname": "john_doe", 
    "result": 23.46, 
    "category": 1 
}
```

**Response body:** <br>
```
{ 
   "result": { 
        "category": 1, 
        "nickname": "john_doe", 
        "result": 23.46, 
        "createdAt": 1717624105670 
    },
	"ranking": 72
}
```