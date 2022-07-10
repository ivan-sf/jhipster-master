import app from '../src/server'
import request from 'supertest'

beforeEach (() => jest. useFakeTimers ()) ;
afterEach (() => jest. useRealTimers ()) ;

describe("GET users", () => {
    test("should respond with a 200 status code", async () => {
        const response = await request(app).get("/api/user").send();
        expect(response.statusCode).toBe(200);
    });

    test("should respond with a array", async () => {
        const response = await request(app).get("/api/user").send();
        expect(response.body).toBeInstanceOf(Array);
    });
});

describe("GET posts", () => {
    test("should respond with a 200 status code", async () => {
        const response = await request(app).get("/api/post").send();
        expect(response.statusCode).toBe(200);
    });

    test("should respond with a array", async () => {
        const response = await request(app).get("/api/post").send();
        expect(response.body).toBeInstanceOf(Array);
    });
});

