package handlers

import (
	"encoding/json"
	"fmt"
	"net/http"

	"github.com/gorilla/mux"
	"github.com/winnerx0/kiroku/internal/db"
	"github.com/winnerx0/kiroku/internal/utils"
)

type Post struct {
	Id     int    `json:"id"`
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

	defer database.Close()

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
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(400)
		w.Write(errorBytes)
		return
	}

	var response = PostResponse{Message: "Added successfully"}
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(&response)

}

func HandleDeletePost(w http.ResponseWriter, r *http.Request) {

	database := db.InitDB()

	defer database.Close()

	vars := mux.Vars(r)

	postId := vars["id"]

	var post Post

	database.QueryRow("SELECT title FROM posts WHERE id = $1", postId).Scan(&post.Title)

	if post.Title == "" {
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(404)
		response, _ := json.Marshal(PostResponse{Message: "Post not foudn"})
		w.Write(response)
		return
	}

	_, err := database.Exec("DELETE FROM posts WHERE id = $1", postId)

	if err != nil {
		w.Header().Set("Content-Type", "application/json")
		errorBytes, _ := json.Marshal(utils.ErrorResponse{Message: err.Error()})
		w.WriteHeader(400)
		w.Write(errorBytes)
		return
	}

	response, _ := json.Marshal(PostResponse{Message: "Deleted successfully"})
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(&response)
}

func HandleGetAllUserPosts(w http.ResponseWriter, r *http.Request) {

	var posts []Post

	database := db.InitDB()

	rows, err := database.Query("SELECT id, title, status, user_id FROM posts")

	defer func() {
		database.Close()
		rows.Close()
	}()

	if err != nil {
		w.Header().Set("Content-Type", "application/json")
		errorBytes, _ := json.Marshal(utils.ErrorResponse{Message: err.Error()})
		w.WriteHeader(400)
		w.Write(errorBytes)
		return
	}

	for rows.Next() {
		var post Post
		if err := rows.Scan(&post.Id, &post.Title, &post.Status, &post.UserId); err != nil {
			w.Header().Set("Content-Type", "application/json")
			errorBytes, _ := json.Marshal(utils.ErrorResponse{Message: err.Error()})
			w.WriteHeader(400)
			w.Write(errorBytes)
			return
		}
		posts = append(posts, post)
	}

	fmt.Println(posts)
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(posts)

}
