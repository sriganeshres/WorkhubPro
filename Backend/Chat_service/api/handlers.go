package api

import (
	"net/http"

	"github.com/labstack/echo/v4"
	"github.com/sriganeshres/WorkHub-Pro/Backend/models"
)

type MessageRequest struct {
	Sender   string `json:"sender"`
	Receiver string `json:"receiver"`
}

func (app *Config) AddMessage(ctx echo.Context) error {
	var message models.Message
	// Use custom time format for parsing the time string
	if err := ctx.Bind(&message); err != nil {

		return ctx.JSON(http.StatusBadRequest, err.Error())
	}

	if errorer := app.Db.CreateMessage(&message); errorer != nil {
		ctx.JSON(http.StatusBadRequest, errorer)
		return errorer
	}

	ctx.JSON(http.StatusCreated, "added message successfully")
	return nil
}
func (app *Config) GetMessage(ctx echo.Context) error {
	var msgreq MessageRequest
	err := ctx.Bind(&msgreq)

	if err != nil {
		return ctx.JSON(http.StatusBadRequest, err.Error())
	}
	var sender = msgreq.Sender
	var receiver = msgreq.Receiver
	messages, errorer := app.Db.FindChat(sender, receiver)
	if errorer != nil {
		ctx.JSON(http.StatusBadRequest, errorer)
		return errorer
	}
	ctx.JSON(http.StatusCreated, messages)
	return nil

}
