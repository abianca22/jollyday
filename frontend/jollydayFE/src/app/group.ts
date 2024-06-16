import { User } from "./user";

export class Group {
    id: number | null;
    name: string | null;
    description: string | null;
    userList: User[] = [];

    constructor(id: number |  null = null, name: string | null = null, description: string | null = null, users: User[] = []) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.userList = Array.of(...users);
    }
}
