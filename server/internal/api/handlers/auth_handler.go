package handlers

import (
	"encoding/json"
	"net/http"

	"github.com/winnerx0/kiroku/internal/db"
	"github.com/winnerx0/kiroku/internal/utils"
)

type UserDTO struct {
	Username string `json:"username"`
	Password string `json:"password"`
}

type TokenResponse struct {
	Token string `json:"token"`
}

func HandleRegister(w http.ResponseWriter, r *http.Request) {

	database := db.InitDB()

	defer database.Close()

	var registerDTO UserDTO

	var user UserDTO

	err := json.NewDecoder(r.Body).Decode(&registerDTO)

	if err != nil {
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(400)
		w.Write([]byte("Error parsing body"))
		return
	}

	database.QueryRow("SELECT username FROM users WHERE username = $1", registerDTO.Username).Scan(&user.Username)

	if user.Username != "" {
		errorBytes, _ := json.Marshal(utils.ErrorResponse{Message: "Username already exists"})
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(409)
		w.Write(errorBytes)
		return
	}

	_, err = database.Exec(`INSERT INTO users (username, password) VALUES ($1, $2)`, registerDTO.Username, registerDTO.Password)

	if err != nil {
		errorBytes, _ := json.Marshal(utils.ErrorResponse{Message: err.Error()})
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(400)
		w.Write(errorBytes)
		return
	}

	token, err := utils.CreateToken(registerDTO.Username)

	if err != nil {
		errorBytes, _ := json.Marshal(utils.ErrorResponse{Message: err.Error()})
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(400)
		w.Write(errorBytes)
		return
	}

	response := TokenResponse{Token: token}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(&response)
}

func HandleLogin(w http.ResponseWriter, r *http.Request) {

	database := db.InitDB()

	defer database.Close()

	var loginDTO UserDTO

	err := json.NewDecoder(r.Body).Decode(&loginDTO)

	if err != nil {
		errorBytes, _ := json.Marshal(utils.ErrorResponse{Message: err.Error()})
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(400)
		w.Write(errorBytes)
		return
	}

	var user UserDTO

	err = database.QueryRow("SELECT username, password FROM users WHERE username = $1", loginDTO.Username).Scan(&user.Username, &user.Password)

	if user.Username == "" {
		errorBytes, _ := json.Marshal(utils.ErrorResponse{Message: "User not found"})
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(404)
		w.Write(errorBytes)
		return
	}

	if loginDTO.Password != user.Password {
		errorBytes, _ := json.Marshal(utils.ErrorResponse{Message: "Invalid credentials"})
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(400)
		w.Write(errorBytes)
		return
	}

	if err != nil {
		errorBytes, _ := json.Marshal(utils.ErrorResponse{Message: err.Error()})
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(400)
		w.Write(errorBytes)
	}

	token, err := utils.CreateToken(loginDTO.Username)

	if err != nil {
		errorBytes, _ := json.Marshal(utils.ErrorResponse{Message: err.Error()})
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(400)
		w.Write(errorBytes)
	}

	response := TokenResponse{Token: token}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(&response)
}
