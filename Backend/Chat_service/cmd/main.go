package main

import (
	"fmt"
	"io"
	"log"
	"net/http"

	"path/filepath"
	"strings"

	"github.com/gorilla/websocket"
	"github.com/joho/godotenv"
	"github.com/labstack/echo/v4"
	"github.com/sriganeshres/WorkHub-Pro/Backend/Chat_service/api"
	"github.com/sriganeshres/WorkHub-Pro/Backend/Chat_service/database"
)

type Server struct {
	rooms map[string]map[*websocket.Conn]bool
	Users []User
}

type User struct {
	name      string
	ws        *websocket.Conn
	connected bool
}

func NewServer() *Server {
	return &Server{
		rooms: make(map[string]map[*websocket.Conn]bool),
	}
}

func (s *Server) handleWS(ws *websocket.Conn, name string) {
	fmt.Println(name)
	users := strings.Split(name, "-")
	sender := users[0]
	receiver := users[1]
	fmt.Println("New incoming connection from client:", ws.RemoteAddr())
	user := User{
		name:      sender,
		ws:        ws,
		connected: true,
	}
	s.Users = append(s.Users, user)
	s.readLoop(ws, receiver)
}

func (s *Server) broadcast(b []byte, receiver string) {
	fmt.Println("Receiver:", receiver)

	for _, user := range s.Users {
		fmt.Print("hello")
		if user.name == receiver && user.connected {
			go func(ws *websocket.Conn) {
				fmt.Println("Sending this data ", b)
				if err := ws.WriteMessage(websocket.TextMessage, b); err != nil {
					fmt.Println("Write error:", err)
				}
			}(user.ws)
			break
		}
	}
}

func (s *Server) readLoop(ws *websocket.Conn, receiver string) {
	defer func() {
		for i, user := range s.Users {
			if user.ws == ws {
				s.Users = append(s.Users[:i], s.Users[i+1:]...)
				break
			}
		}
	}()

	for {
		_, msg, err := ws.ReadMessage()
		if err != nil {
			if err == io.EOF {
				break
			}
			fmt.Println("Read error: ", err)
			continue
		}
		s.broadcast(msg, receiver)
	}
}

func getRoomName(c echo.Context) string {
	roomName := c.QueryParam("room")
	if roomName == "" {
		roomName = "default"
	}
	return roomName
}

func main() {
	server := NewServer()
	e := echo.New()
	db := database.NewDatabase()
	envPath := filepath.Join( "..", ".env")
	absEnvPath, err := filepath.Abs(envPath)
    if err != nil {
        fmt.Println("Error getting absolute path:", err)
        return
    }
	er := godotenv.Load(absEnvPath)
	if er != nil {
		log.Println(er)
	}

	app := api.Config{Router: e, Db: db}
	app.Routes()

	err = app.Db.Init()
	if err != nil {
		panic(err)
	}
	err = app.Db.Migrate()
	if err != nil {
		log.Fatal(err)
	}

	upgrader := websocket.Upgrader{
		CheckOrigin: func(r *http.Request) bool {
			return true
		},
	}

	app.Router.GET("/ws", func(c echo.Context) error {
		fmt.Println("Someone wants to connect")
		roomName := getRoomName(c)

		ws, err := upgrader.Upgrade(c.Response(), c.Request(), nil)
		if err != nil {
			log.Println("Failed to upgrade to WebSocket:", err)
			return err
		}
		defer ws.Close()

		fmt.Println("WebSocket connection established")
		server.handleWS(ws, roomName)
		return nil
	})

	fmt.Println("WebSocket server running on localhost:8002")
	e.Logger.Fatal(e.Start(":8002"))
}
