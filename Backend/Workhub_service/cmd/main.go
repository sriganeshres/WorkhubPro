package main

import (
	"log"
	"path/filepath"

	"github.com/joho/godotenv"
	"github.com/labstack/echo/v4"
	"github.com/sriganeshres/WorkHub-Pro/Backend/Workhub_service/api"
	"github.com/sriganeshres/WorkHub-Pro/Backend/Workhub_service/database"
)

func main() {
	e := echo.New()
	envPath := filepath.Join( "..", ".env")
	er := godotenv.Load(envPath)

	db := database.NewDatabase()
	if er != nil {
		panic(er)
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
	e.Logger.Fatal(e.Start(":8001"))
}
