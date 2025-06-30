package main

import (
	"fmt"
	"net/http"

	"github.com/winnerx0/kiroku/internal/api"
	"github.com/winnerx0/kiroku/internal/db"
)

func main() {

	db.InitDB()

	router := api.NewRouter()

	fmt.Println("Listening to port 8000")

	http.ListenAndServe(":8000", router)
}
