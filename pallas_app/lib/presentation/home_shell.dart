import 'package:flutter/material.dart';

import 'my_bubble_story_screen.dart';
import 'post_story_screen.dart';

enum AppScreen { myBubbleStory, postStory }

class HomeShell extends StatefulWidget {
  const HomeShell({super.key});

  @override
  State<HomeShell> createState() => _HomeShellState();
}

class _HomeShellState extends State<HomeShell> {
  AppScreen _current = AppScreen.myBubbleStory;

  void _navigate(AppScreen screen) {
    setState(() => _current = screen);
    Navigator.of(context).pop();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: Text(
          _current == AppScreen.myBubbleStory
              ? 'Stories of my bubble'
              : 'Post story',
        ),
      ),
      drawer: Drawer(
        child: ListView(
          padding: EdgeInsets.zero,
          children: [
            DrawerHeader(
              decoration: BoxDecoration(
                color: Theme.of(context).colorScheme.inversePrimary,
              ),
              child: const Text('Pallas', style: TextStyle(fontSize: 24)),
            ),
            ListTile(
              leading: const Icon(Icons.bubble_chart),
              title: const Text('Stories of my bubble'),
              selected: _current == AppScreen.myBubbleStory,
              onTap: () => _navigate(AppScreen.myBubbleStory),
            ),
            ListTile(
              leading: const Icon(Icons.edit),
              title: const Text('Post story'),
              selected: _current == AppScreen.postStory,
              onTap: () => _navigate(AppScreen.postStory),
            ),
          ],
        ),
      ),
      body: _current == AppScreen.myBubbleStory
          ? const MyBubbleStoryScreen()
          : const PostStoryScreen(),
    );
  }
}
