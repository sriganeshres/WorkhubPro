postgresinit:
	docker run --name postgresGo -p 5432:5432 -e POSTGRES_USER=root -e POSTGRES_PASSWORD=password -d postgres:15-alpine
postgres:
	docker exec -it postgresGo psql
createdb:
	docker exec -it postgresGo createdb --username=root --owner=root $(name)
dropdb:
	docker exec -it postgresGo dropdb -U root $(name)
bash:
	docker exec -it postgresGo bash
runapp:
	go run .\Workhub_service\cmd\main.go ; go run .\Authentication_service\cmd\main.go

.PHONY: postgresinit postgres createdb dropdb runapp 