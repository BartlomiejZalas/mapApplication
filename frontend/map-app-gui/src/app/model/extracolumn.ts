export class ExtraColumn {
  attr: string;
  presentationName: string;

  constructor(attr: string, presentationName: string) {
    this.presentationName = presentationName;
    this.attr = attr;
  }
}
