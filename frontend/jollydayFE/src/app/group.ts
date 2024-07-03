import { User } from "./user";

export class Group {
    id: number | null;
    name: string | null;
    description: string | null;
    leaderId: number | null;
    userList: User[] = [];

    constructor(id: number |  null = null, name: string | null = null, description: string | null = null, leader: number | null = null, users: User[] = []) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.leaderId = leader;
        this.userList = Array.of(...users);
    }
}
