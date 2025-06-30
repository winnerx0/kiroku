package api

import (
	"github.com/gorilla/mux"
	"github.com/winnerx0/kiroku/internal/api/handlers"
)

func NewRouter() *mux.Router {

	router := mux.NewRouter()

	router.HandleFunc("/api/register", handlers.HandleRegister).Methods("POST")

	router.HandleFunc("/api/login", handlers.HandleLogin).Methods("POST")

	router.HandleFunc("/api/post", handlers.HandleCreatePost).Methods("POST")

	router.HandleFunc("/api/post/{id}", handlers.HandleDeletePost).Methods("DELETE")

	return router
}
