package org.main;

public class MusicPlay {
	private String[] musicGender = {"Rock", "Pop", "Country", "Jazz", "Classic"};
	private String[] music = {"Day and Night", "Flora Cash", "Another Love", "Lost on You"};

	// public MusicPlay(String[] musicGender, String [] music) {
	// 	this.musicGender = musicGender;
	// 	this.music = music;
	// }

	public String[] getMusicGender() {
		return musicGender;
	}

	public void setMusicGender(String[] musicGender) {
		this.musicGender = musicGender;
	}

	public String[] getMusic() {
		return music;
	}

	public void setMusic(String[] music) {
		this.music = music;
	}

	// go through a array and verify the String argument 
	public String musicToPlay(String choice) {
		for (String str : music) {
			if (str.equals(choice)) {
				return choice;
			}
		}
		return "-1";
	}

	public void musicStop() {
		System.out.println("Music stopped");
	}
}
