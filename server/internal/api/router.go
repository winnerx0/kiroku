package api

import (
	"net/http"

	"github.com/winnerx0/kiroku/internal/api/handlers"
)

func NewRouter() *http.ServeMux {

	mux := http.NewServeMux()

	mux.HandleFunc("POST /api/register", handlers.HandleRegister)

	mux.HandleFunc("POST /api/login", handlers.HandleLogin)

	mux.HandleFunc("POST /api/post", handlers.HandleCreatePost)

	return mux
}
