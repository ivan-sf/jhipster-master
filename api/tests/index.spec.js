"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const server_1 = __importDefault(require("../src/server"));
const supertest_1 = __importDefault(require("supertest"));
beforeEach(() => jest.useFakeTimers());
afterEach(() => jest.useRealTimers());
describe("GET users", () => {
    test("should respond with a 200 status code", () => __awaiter(void 0, void 0, void 0, function* () {
        const response = yield (0, supertest_1.default)(server_1.default).get("/api/user").send();
        expect(response.statusCode).toBe(200);
    }));
    test("should respond with a array", () => __awaiter(void 0, void 0, void 0, function* () {
        const response = yield (0, supertest_1.default)(server_1.default).get("/api/user").send();
        expect(response.body).toBeInstanceOf(Array);
    }));
});
describe("GET posts", () => {
    test("should respond with a 200 status code", () => __awaiter(void 0, void 0, void 0, function* () {
        const response = yield (0, supertest_1.default)(server_1.default).get("/api/post").send();
        expect(response.statusCode).toBe(200);
    }));
    test("should respond with a array", () => __awaiter(void 0, void 0, void 0, function* () {
        const response = yield (0, supertest_1.default)(server_1.default).get("/api/post").send();
        expect(response.body).toBeInstanceOf(Array);
    }));
});
