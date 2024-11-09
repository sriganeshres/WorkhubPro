package main

import (
	"log"
	"path/filepath"

	"github.com/joho/godotenv"
	"github.com/labstack/echo/v4"
	"github.com/sriganeshres/WorkHub-Pro/Backend/Authentication_service/api"
	"github.com/sriganeshres/WorkHub-Pro/Backend/Authentication_service/database"
)

func main() {
	e := echo.New()
	db := database.NewDatabase()
	envPath := filepath.Join("..", ".env")
	er := godotenv.Load(envPath)
	if er != nil {
		log.Println(er)
	}
	app := api.Config{Router: e, Db: db}
	app.Routes()
	err := app.Db.Init()
	print(err)
	if err != nil {
		panic(err)
	}
	err = app.Db.Migrate()
	if err != nil {
		log.Fatal(err)
	}
	e.Logger.Fatal(e.Start(":8000"))
}
