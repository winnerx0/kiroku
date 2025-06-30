package utils

import (
	"time"

	"github.com/golang-jwt/jwt/v5"
)

var secretKey = []byte("c4f2d2a1b0e3c1f8e0c1a9d2e3f1a7c9f2b3d1e8c9a2b1d3e4f1c2a9b8d7e6")

func CreateToken(username string) (string, error) {

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, jwt.MapClaims{"username": username, "exp": time.Now().Add(time.Hour * 24).Unix()})

	tokenString, err := token.SignedString(secretKey)

	return tokenString, err
}
