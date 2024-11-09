package api

import (
	"github.com/labstack/echo/v4"
	"github.com/sriganeshres/WorkHub-Pro/Backend/Authentication_service/database"
)

type Config struct {
	Router *echo.Echo
	Db     *database.Database
}

func (app *Config) Routes() {
	app.Router.POST("/api/login", app.Login)
	app.Router.POST("/api/signup", app.Signup)
	app.Router.POST("/api/sendmail", app.SendEmailHandler)
	app.Router.POST("api/token", app.VerifyToken)
	app.Router.GET("/api/user", app.GetUser)
	app.Router.POST("/api/getallemployees",app.GetAllEmployees)
	app.Router.POST("/api/getallprojectleads",app.GetAllProjectLeads)
	app.Router.POST("/api/addUsers", app.AddUsersToProject)
}
