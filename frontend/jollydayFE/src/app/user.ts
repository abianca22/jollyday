import { Group } from "./group";

export class User {
    id: number | null;
    username: string | null;
    email: string | null;
    password: string | null;
    firstName: string | null;
    lastName: string | null;
    birthday: string | null;
    group: Group | null;
    role: String | null;
    jwtToken: String | null;

    constructor(username: string | null = null,
                email: string | null = null, 
                password: string | null = null, 
                firstName: string | null = null, 
                lastName: string | null = null, 
                birthday: string | null = null, 
                id: number | null = null, 
                group: Group | null = null, 
                role: String | null = null,
                token: String | null = null) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.group = group;
        this.password = password;
        this.role = role;
        this.jwtToken = token;
    }

}
