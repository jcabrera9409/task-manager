import { User } from "./user";

export class Task {
    id: number;
    title: string;
    description: string;
    completed: boolean;
    createdAt: string;
    updatedAt: string;
    user: User;
}