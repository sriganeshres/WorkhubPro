package api

import (
	"log"
	"net/http"
	"net/http/httptest"
	"path/filepath"
	"strings"
	"testing"

	"github.com/joho/godotenv"
	"github.com/labstack/echo/v4"
	"github.com/sriganeshres/WorkHub-Pro/Backend/Authentication_service/database"
	"github.com/stretchr/testify/assert"
)

func SetupTestApp() *Config {
	e := echo.New()
	db := database.NewDatabase()
	envPath := filepath.Join("..", "..", ".env")
	_ = godotenv.Load(envPath)

	app := Config{Router: e, Db: db}
	err := app.Db.Init()
	print(err)
	if err != nil {
		panic(err)
	}
	err = app.Db.Migrate()
	if err != nil {
		log.Fatal(err)
	}
	app.Routes()
	return &app
}

func TestSignup(t *testing.T) {
	// Setup
	app := SetupTestApp()

	// Normal email signup
	normalEmailBody := `{"username":"TestDDUsFFFer","email":"test3@example.com","password":"password123,"id":1}`
	normalEmailReq := httptest.NewRequest(http.MethodPost, "/api/signup", NewReader(normalEmailBody))
	normalEmailReq.Header.Set(echo.HeaderContentType, echo.MIMEApplicationJSON)
	normalEmailRec := httptest.NewRecorder()
	normalEmailCtx := app.Router.NewContext(normalEmailReq, normalEmailRec)
	err1 := app.Signup(normalEmailCtx)

	// Duplicate email signup
	duplicateEmailBody := `{"username":"TestUsssser","email":"test3@example.com","password":"password123","id":1}`
	duplicateEmailReq := httptest.NewRequest(http.MethodPost, "/api/signup", NewReader(duplicateEmailBody))
	duplicateEmailReq.Header.Set(echo.HeaderContentType, echo.MIMEApplicationJSON)
	duplicateEmailRec := httptest.NewRecorder()
	duplicateEmailCtx := app.Router.NewContext(duplicateEmailReq, duplicateEmailRec)
	err2 := app.Signup(duplicateEmailCtx)

	// Weak password signup
	weakPasswordBody := `{"username":"WeakUser","email":"tested@example.com","password":"123","id":1}`
	weakPasswordReq := httptest.NewRequest(http.MethodPost, "/api/signup", NewReader(weakPasswordBody))
	weakPasswordReq.Header.Set(echo.HeaderContentType, echo.MIMEApplicationJSON)
	weakPasswordRec := httptest.NewRecorder()
	weakPasswordCtx := app.Router.NewContext(weakPasswordReq, weakPasswordRec)
	err3 := app.Signup(weakPasswordCtx)

	weakPassword2Body := `{"username":"WeakUser","email":"weak@example.com","password":"123","id":1}`
	weakPassword2Req := httptest.NewRequest(http.MethodPost, "/api/signup", NewReader(weakPassword2Body))
	weakPassword2Req.Header.Set(echo.HeaderContentType, echo.MIMEApplicationJSON)
	weakPassword2Rec := httptest.NewRecorder()
	weakPassword2Ctx := app.Router.NewContext(weakPassword2Req, weakPassword2Rec)
	err4 := app.Signup(weakPassword2Ctx)

	// Assertions
	if assert.NoError(t, err1) {
		println("no error")
	}
	if assert.Error(t, err2) {
		assert.Equal(t, "already the email is in use", err2.Error())
	}
	if assert.Error(t, err3) {
		assert.Equal(t, "password must be at least 8 characters long", err3.Error())
	}
	if assert.Error(t, err4) {
		assert.Equal(t, "password must be at least 8 characters long", err3.Error())
	}

}

func NewReader(reqBody string) *strings.Reader {
	return strings.NewReader(reqBody)
}
