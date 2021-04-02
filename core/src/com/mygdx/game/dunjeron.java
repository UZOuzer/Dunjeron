package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;


public class dunjeron extends ApplicationAdapter {
	SpriteBatch batch;
	//Texture img1;
	//Texture imggeroi;
	Sprite platform;


	Hero imggeroi;

	Sprite knopkaverx;
	Sprite bochka;
	Rectangle[] rectplatformsmini;
	Sprite jamp;
	Rectangle rectgeroi;

	Rectangle rectplatfor;


	Platforms[] platformmini;


	class Hero{ // класс для героя
		boolean isStay = true;
		float positionX, positionY;
		float dX, dY;
		Sprite currentSprite, leftSprite, rightSprite;
		boolean padenie = true;
		boolean isFly = false;

		// обновляем значения до начальных
		public Hero(float positionX, float positionY, Sprite rightSprite, Sprite leftSprite) {
			dX = 0;
			dY = 0;
			this.positionX = positionX; // this - именно значение в классе
			this.positionY = positionY;
			this.leftSprite = leftSprite;
			this.rightSprite = rightSprite;

			this.currentSprite = rightSprite;
		}
		// рисуем
		void draw(Batch batch){
			batch.draw(currentSprite, positionX, positionY);
			update();
		}
		// обновляем
		void update(){
			positionX += dX;
			positionY += dY;
			currentSprite.setX(positionX);
			currentSprite.setY(positionY);
			dY -= 1;
		}
		// герой стоит на месте
		void stay(){
			dY = 0;
			isStay = true;
		}
		// герой не стоит на платформе
		void fall(){
			dY = -1;
			isStay = false;
		}
		// прыжок
		void jump(){
			if (dY == 0 && isStay){
				dY = 13;
				isStay = false;
			}
		}

		public void goRight() {
			dX = 10;
			currentSprite = rightSprite;
		}


		public void goLeft() {
			dX = -10;
			currentSprite = leftSprite;
		}
	}





    // массив платформ
	public void platformsfunction(){
		Random random = new Random();

		platformmini = new Platforms[3];  // создание массива
		for(int i = 0; i < platformmini.length; i++){
			platformmini[i] = new Platforms(new Texture("platformmini.bmp"));
		}


	}

	public void fisika(){

		if(rectplatfor.overlaps(imggeroi.currentSprite.getBoundingRectangle())){
			imggeroi.stay();
		}
		for (int i = 0; i < rectplatformsmini.length; i++){
			if(rectplatformsmini[i].overlaps(imggeroi.currentSprite.getBoundingRectangle())){
				//imggeroi.positionX -= imggeroi.dX;
				imggeroi.positionY -= imggeroi.dY;
				imggeroi.dY = 0;
				//imggeroi.dX = 0;

			}
		}

	}


	@Override  // создание разных штук
	public void create () {
		//platformsfunction();
		batch = new SpriteBatch();

		imggeroi = new Hero(0, 300, new Sprite(new Texture("geroipravo.png")), new Sprite(new Texture("geroilevo.png")));

		platform = new Sprite(new Texture("platform.bmp"));
		knopkaverx = new Sprite(new Texture("badlogic.jpg"));
		bochka = new Sprite(new Texture("bochka.bmp"));

		bochka.setX(1730);
		bochka.setY(10);
		jamp = new Sprite(new Texture("jamp.bmp"));
		jamp.setX(1100);
		jamp.setY(12);




		// считывание с файла формата txt координат платформ
		try {
			File file = new File("android/assets/platforms.txt");
			Scanner scanner = new Scanner(file);

			platformmini = new Platforms[3];

			for (int i = 0; i < platformmini.length; i++){
				if (scanner.hasNextInt()){
					platformmini[i] = new Platforms(new Texture("platformmini.bmp"));
					platformmini[i].setX(scanner.nextInt());
					platformmini[i].setY(scanner.nextInt());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		rectplatformsmini = new Rectangle[platformmini.length];
		for(int i = 0; i < rectplatformsmini.length;i++){
			rectplatformsmini[i] = platformmini[i].getBoundingRectangle();
		}
	}

	@Override // управление и логика
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1); // экран
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // что-то связанное со цветом

		rectplatfor = platform.getBoundingRectangle();
		rectgeroi = imggeroi.currentSprite.getBoundingRectangle();


		Rectangle rectbochka = bochka.getBoundingRectangle();
		Rectangle rectjamp = jamp.getBoundingRectangle(); // создаём джампер
		Rectangle cnopochka = knopkaverx.getBoundingRectangle(); // создаём прямоугольник кнопки


		fisika();

		if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.SPACE)){
			imggeroi.jump();
		}
		else {
			if (Gdx.input.isKeyPressed(Input.Keys.D)){
				imggeroi.goRight();
			}
			else{
				if (Gdx.input.isKeyPressed(Input.Keys.A)){
					imggeroi.goLeft();
				}
				else {
					imggeroi.dX = 0;
				}
			}
		}

		batch.begin();
		platform.draw(batch);
		imggeroi.draw(batch);
		jamp.draw(batch);
		bochka.draw(batch);

		for(int i = 0; i < platformmini.length; i++){
			platformmini[i].draw(batch);
		}

		batch.end();
	}

	@Override // уничтожение картинок
	public void dispose () {
		batch.dispose();

	}
}
