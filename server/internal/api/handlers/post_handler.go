package handlers

import (
	"encoding/json"
	"fmt"
	"net/http"

	"github.com/winnerx0/kiroku/internal/db"
	"github.com/winnerx0/kiroku/internal/utils"
)

type Post struct {
	Title  string `json:"title"`
	Status Status `json:"status"`
	UserId int    `json:"user_id"`
}

type PostResponse struct {
	Message string `json:"message"`
}

type Status string

const (
	NOT_STARTED Status = "NOT_STARTED"
	ON_GOING    Status = "ON_GOING"
	COMPLETED   Status = "COMPLETED"
)

func HandleCreatePost(w http.ResponseWriter, r *http.Request) {

	database := db.InitDB()

	var post Post

	err := json.NewDecoder(r.Body).Decode(&post)

	fmt.Print(post)

	if err != nil {
		http.Error(w, err.Error(), 400)
		return
	}

	_, err = database.Exec("INSERT INTO posts (title, status, user_id) VALUES ($1, $2, $3)", post.Title, post.Status, post.UserId)

	if err != nil {
		errorBytes, _ := json.Marshal(utils.ErrorResponse{Message: err.Error()})
		w.WriteHeader(400)
		w.Write(errorBytes)
		w.Header().Set("Content-Type", "application/json")
		return
	}

	var response = PostResponse{Message: "Added successfully"}
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(&response)

}
