package api

import (
	"net/http"
	"strings"

	"github.com/golang-jwt/jwt/v4"
	"github.com/labstack/echo/v4"
)

func JwtMiddleware(next echo.HandlerFunc) echo.HandlerFunc {
	return func(c echo.Context) error {
		authHeader := c.Request().Header.Get("Authorization")
		if authHeader == "" {
			return c.String(http.StatusUnauthorized, "Missing JWT token")
		}

		// Check if the Authorization header has the format "Bearer <token>"
		parts := strings.Split(authHeader, " ")
		if len(parts) != 2 || parts[0] != "Bearer" {
			return c.String(http.StatusUnauthorized, "Invalid Authorization header format")
		}

		tokenString := parts[1]

		// Extract JWT token from the Authorization header
		token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
			// Verify signing method and return secret key or public key
			// In this example, we're using a secret key
			return []byte("your_secret_key"), nil
		})

		if err != nil || !token.Valid {
			return c.String(http.StatusUnauthorized, "Invalid JWT token")
		}

		// Store the user ID or other information from the token in the context
		claims := token.Claims.(jwt.MapClaims)
		userID := claims["email"].(string)
		c.Set("email", userID)

		// Call the next handler in the chain
		return next(c)
	}
}
