package api

import (
	"errors"
	"fmt"
	"log"
	"net/http"
	"time"

	"github.com/golang-jwt/jwt/v5"

	"github.com/labstack/echo/v4"
	"github.com/sriganeshres/WorkHub-Pro/Backend/Authentication_service/utils"
	"github.com/sriganeshres/WorkHub-Pro/Backend/models"
)

func (app *Config) Login(ctx echo.Context) error {
	log.Println("A try to login")
	var userData models.LoginUser
	err := ctx.Bind(&userData)
	if err != nil {
		return ctx.JSON(http.StatusBadRequest, err.Error())
	}
	email := userData.Email
	password := userData.Password
	user, err := app.Db.GetUserByEmail(email)
	if err != nil {
		ctx.JSON(http.StatusBadRequest, "Invalid password")
	}
	if user == nil {
		return ctx.JSON(http.StatusBadRequest, "Invalid credentials")
	}
	whether, erro := utils.VerifyPassword(password, user.Password)
	if !whether {
		fmt.Println(erro.Error())
		ctx.JSON(http.StatusBadRequest, "Invalid password")
		return errors.New(erro.Error())
	}

	// Set custom claims
	token := jwt.New(jwt.SigningMethodHS256)
	claims := token.Claims.(jwt.MapClaims)
	claims["email"] = user.Email
	claims["exp"] = time.Now().Add(time.Hour * 24).Unix()
	t, err := token.SignedString([]byte("secret"))
	if err != nil {
		return err
	}
	println(user)
	println(t)
	ctx.JSON(http.StatusCreated, echo.Map{
		"token": t,
		"user":  user,
	})

	return nil
}
func (app *Config) VerifyToken(ctx echo.Context) error {

	var tokenString string
	err := ctx.Bind(&tokenString)
	if err != nil {
		return ctx.JSON(http.StatusBadRequest, echo.Map{"user": nil,
			"success": false})
	}
	token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
		return []byte("secret"), nil
	})
	if err != nil {
		return err
	}
	claims, ok := token.Claims.(jwt.MapClaims)
	if !ok {
		return ctx.JSON(http.StatusUnauthorized, echo.Map{"user": nil,
			"success": false})
	}

	// Extract email from claims
	email, ok := claims["email"].(string)
	if !ok {
		return ctx.JSON(http.StatusUnauthorized, echo.Map{"user": nil,
			"success": false})
	}

	// Now you have the email extracted from the token
	// Use it as needed in your application logic
	user, err := app.Db.GetUserByEmail(email)
	if err != nil {
		return err
	}
	return ctx.JSON(http.StatusOK, echo.Map{"user": user,
		"success": true,
	})

}

func (app *Config) Signup(ctx echo.Context) error {
	body := ctx.Request().Body
	defer body.Close()

	// Create a byte slice to hold the body content
	var data []byte
	_, err := body.Read(data)
	if err != nil {
		return ctx.String(http.StatusInternalServerError, "Error reading request body")
	}

	var userData models.UserData
	err1 := ctx.Bind(&userData)
	// userData.Role = "admin"

	if err1 != nil {

		return ctx.JSON(http.StatusBadRequest, err1.Error())
	}
	password := userData.Password
	if password == "" || len(password) <= 8 {
		return fmt.Errorf("password must be at least 8 characters long")
	}
	if user, er := app.Db.GetUserByEmail(userData.Email); user != nil {
		if er == nil {
			return fmt.Errorf("already the email is in use")
		}
	}
	userData.Password = utils.HashString(password)
	if errorer := app.Db.CreateUser(&userData); errorer != nil {
		ctx.JSON(http.StatusBadRequest, errorer)
		return errorer
	}
	token := jwt.New(jwt.SigningMethodHS256)
	claims := token.Claims.(jwt.MapClaims)
	claims["email"] = userData.Email
	claims["exp"] = time.Now().Add(time.Hour * 24).Unix()
	t, err := token.SignedString([]byte("secret"))
	if err != nil {
		return err
	}

	ctx.JSON(http.StatusCreated, echo.Map{
		"user":  userData,
		"token": t,
	})
	return nil
}

func (app *Config) SendEmailHandler(ctx echo.Context) error {
	var requestData struct {
		Email string `json:"email"`
		Code  int    `json:"code"`
	}

	if err := ctx.Bind(&requestData); err != nil {
		return ctx.JSON(http.StatusBadRequest, map[string]string{"error": "Invalid request"})
	}
	email := requestData.Email
	code := requestData.Code

	var err = utils.SendEmail(email, code)
	if err != nil {
		return ctx.JSON(http.StatusInternalServerError, echo.Map{
			"msg":     err.Error(),
			"success": false,
		})
	}
	return ctx.JSON(http.StatusInternalServerError, echo.Map{
		"msg":     "sent mail",
		"success": true,
	})
}

func (app *Config) GetUser(ctx echo.Context) error {
	email := ctx.QueryParam("email")
	if email == "" {
		ctx.JSON(http.StatusBadRequest, "Please provide an email address")
		return errors.New("email address is not provided")
	}
	fmt.Print(email)
	user, err := app.Db.GetUserByEmail(email)
	if err != nil {
		ctx.JSON(http.StatusBadRequest, err.Error())
		return err
	}
	ctx.JSON(http.StatusOK, user)
	return nil
}

func (app *Config) GetAllEmployees(ctx echo.Context) error {
	println("hi hello how are you")
	var requestData struct {
		WorkhubId uint `json:"workhub_id"`
	}
	if err := ctx.Bind(&requestData); err != nil {
		return ctx.JSON(http.StatusBadRequest, map[string]string{"error": "Invalid request"})
	}
	println(requestData.WorkhubId)
	users, err := app.Db.GetAllEmplyeesbyWorkhubId(requestData.WorkhubId)
	if len(users) == 0 {
		println("any errors")
		return ctx.JSON(http.StatusBadRequest, "No employees found")
	}
	if err != nil {
		println("what is the error")
		println(err.Error())
		return ctx.JSON(http.StatusBadRequest, err.Error())
	}
	fmt.Println(users)
	ctx.JSON(http.StatusOK, users)
	return nil
}

func (app *Config) GetAllProjectLeads(ctx echo.Context) error {
	var requestData struct {
		WorkhubId uint `json:"workhub_id"`
	}
	if err := ctx.Bind(&requestData); err != nil {
		return ctx.JSON(http.StatusBadRequest, map[string]string{"error": "Invalid request"})
	}
	users, err := app.Db.GetAllProjectLeadbyWorkhubId(requestData.WorkhubId)
	if len(users) == 0 {
		return ctx.JSON(http.StatusBadRequest, "No project leads found")
	}
	if err != nil {
		return ctx.JSON(http.StatusBadRequest, err.Error())
	}
	ctx.JSON(http.StatusOK, users)
	return nil
}
func (app *Config) AddUsersToProject(ctx echo.Context) error {
	var dataIncoming models.AddUsersToProject
	err := ctx.Bind(&dataIncoming)
	if err != nil {
		ctx.JSON(http.StatusBadRequest, err.Error())
		return err
	}
	if errorer := app.Db.AddUsersToProject(dataIncoming); errorer != nil {
		ctx.JSON(http.StatusBadRequest, errorer)
		return errorer
	}
	ctx.JSON(http.StatusOK, "Added Successfully")
	return nil
}
