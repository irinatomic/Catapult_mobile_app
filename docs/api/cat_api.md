# The Cat API Documentation

The official [The Cat API](https://thecatapi.com/) is used for fetching information about cats. 

## Endpoints used

| Method | Endpoint |
|--------|--------------------------------------------|
| GET    | https://api.thecatapi.com/v1/breeds        |
| GET    | https://api.thecatapi.com/v1/images/search |

## Fetching All Breeds
To fetch information about all cat breeds, use the following endpoint: <br>
**GET** `https://api.thecatapi.com/v1/breeds`

**Header Parameters** <br>
- `x-api-key` (required): Your API key


## Fetching Images for a Specific Breed
To fetch images for a specific cat breed, use the following endpoint: <br>
**GET** `https://api.thecatapi.com/v1/images/search`

**Header Parameters:** <br>
- `x-api-key` (required): Your API key

**URL Parameters:** <br>
- `breed_ids` (required): The ID of the breed for which you want to fetch images (e.g., 'abys' for Abyssinian)
- `limit` (optional): The number of images to return (if not specified, only one image is returned)

Example request for fetching up to 100 images of a specific breed: <br>
**GET** `https://api.thecatapi.com/v1/images/search?breed_ids=abys&limit=100`