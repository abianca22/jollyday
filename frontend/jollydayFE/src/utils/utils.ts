export class Utils { 
    
    public static formatDate(date: string | null): string | null {
    if (date === null) return null;
    const day = date.split(/\-|\./)[2];
    const month = date.split(/\-|\./)[1];
    const year = date.split(/\-|\./)[0];
    return `${day}.${month}.${year}`;
  }

    public static deformatDate(date: string | null): string | null {
    if (date === null) return null;
    const year = date.split(/\-|\./)[2];
    const month = date.split(/\-|\./)[1];
    const day = date.split(/\-|\./)[0];
    return `${year}-${month}-${day}`;
  }
}